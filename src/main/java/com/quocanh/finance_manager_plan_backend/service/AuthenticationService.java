package com.quocanh.finance_manager_plan_backend.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.quocanh.finance_manager_plan_backend.dto.request.UserDTO;
import com.quocanh.finance_manager_plan_backend.dto.request.auth.AuthenticationRequest;
import com.quocanh.finance_manager_plan_backend.dto.request.auth.IntrospectRequest;
import com.quocanh.finance_manager_plan_backend.dto.request.auth.LogoutRequest;
import com.quocanh.finance_manager_plan_backend.dto.request.auth.TokenRequest;
import com.quocanh.finance_manager_plan_backend.dto.response.auth.AuthenticationResponse;
import com.quocanh.finance_manager_plan_backend.dto.response.auth.IntrospectResponse;
import com.quocanh.finance_manager_plan_backend.exception.AppException;
import com.quocanh.finance_manager_plan_backend.exception.ErrorCode;
import com.quocanh.finance_manager_plan_backend.entity.InvalidatedOTP;
import com.quocanh.finance_manager_plan_backend.entity.Users;
import com.quocanh.finance_manager_plan_backend.mapper.AuthenticationMapper;
import com.quocanh.finance_manager_plan_backend.mapper.UserMapper;
import com.quocanh.finance_manager_plan_backend.repository.InvalidatedOTPRepo;
import com.quocanh.finance_manager_plan_backend.repository.UsersRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthenticationService {
    UsersRepo userRepo;
    InvalidatedOTPRepo invalidatedOTPRepo;
    AuthenticationMapper authenticationMapper;
    UserMapper userMapper;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    //Kiểm tra token
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        }catch (AppException e){
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    //Xác thực tài khoản
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Users user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());

        if (!authenticated) {
            throw new AppException(ErrorCode.AUTHENTICATION_FAILED);
        }

        var token = generateToken(user);
        var expiresAt = getTokenExpirationTime(token);

        // Convert user to UserDTO first
        UserDTO userDTO = userMapper.toDTO(user);

        // Use mapper to create AuthenticationResponse
        return authenticationMapper.userToAuthResponse(userDTO, token, true, expiresAt);
    }

    //làm mới token
    public AuthenticationResponse refreshToken(TokenRequest request) throws ParseException, JOSEException {
        var verifiedToken = verifyToken(request.getToken(),true);
        var jit = verifiedToken.getJWTClaimsSet().getJWTID();
        var expiryTime = verifiedToken.getJWTClaimsSet().getExpirationTime();
        InvalidatedOTP invalidatedOTP = InvalidatedOTP.builder()
                .id(jit)
                .expiryDate(expiryTime)
                .build();
        invalidatedOTPRepo.save(invalidatedOTP);

        var useName = verifiedToken.getJWTClaimsSet().getSubject();
        var user = userRepo.findByEmail(useName)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND));
        var token = generateToken(user);
        var expiresAt = getTokenExpirationTime(token);

        // Convert user to UserDTO
        UserDTO userDTO = userMapper.toDTO(user);

        // Use mapper to create AuthenticationResponse
        return authenticationMapper.userToAuthResponse(userDTO, token, true, expiresAt);
    }

    //Lấy thời gian hết hạn của token
    private Date getTokenExpirationTime(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getExpirationTime();
        } catch (ParseException e) {
            log.error("Error parsing token", e);
            throw new AppException(ErrorCode.TOKEN_PARSING_FAILED);
        }
    }

    //đăng xuất và lưu token vào danh sách đã hủy
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var SignToken = verifyToken(request.getToken(), true);
            String jit = SignToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = SignToken.getJWTClaimsSet().getExpirationTime();
            InvalidatedOTP invalidatedOTP = InvalidatedOTP.builder()
                    .id(jit)
                    .expiryDate(expiryTime)
                    .build();
            invalidatedOTPRepo.save(invalidatedOTP);
        }
        catch (AppException e){
            log.info("Token is invalid");
        }
    }

    //Xác thực token
    public SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date( signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(jwsVerifier);

        if (!(verified || expiryTime.after(new Date())))
            throw new AppException(ErrorCode.TOKEN_INVALID);

        if(invalidatedOTPRepo.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.TOKEN_INVALID);
        return signedJWT;
    }

    //Tạo token
    private String generateToken(Users user) {
        //Header
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        //Body
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("finance_manager_plan")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        //Payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error generating token", e);
            throw new AppException(ErrorCode.TOKEN_GENERATION_FAILED);
        }
    }

    //Tạo scope
    private String buildScope(Users user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (user.getRole() != null) {
            stringJoiner.add(user.getRole().name());
        }
        return stringJoiner.toString();
    }
}


