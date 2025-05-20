package com.quocanh.finance_manager_plan_backend.service;

import com.quocanh.finance_manager_plan_backend.dto.request.UserDTO;
import com.quocanh.finance_manager_plan_backend.dto.request.auth.RegisterRequest;
import com.quocanh.finance_manager_plan_backend.entity.Users;
import com.quocanh.finance_manager_plan_backend.entity.VerificationOTP;
import com.quocanh.finance_manager_plan_backend.enums.UserRoleEnum;
import com.quocanh.finance_manager_plan_backend.exception.AppException;
import com.quocanh.finance_manager_plan_backend.exception.ErrorCode;
import com.quocanh.finance_manager_plan_backend.mapper.AuthenticationMapper;
import com.quocanh.finance_manager_plan_backend.mapper.UserMapper;
import com.quocanh.finance_manager_plan_backend.repository.InvalidatedOTPRepo;
import com.quocanh.finance_manager_plan_backend.repository.UsersRepo;
import com.quocanh.finance_manager_plan_backend.repository.VerificationOtpRepo;
import com.quocanh.finance_manager_plan_backend.util.getUserCurrent;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService {
    UsersRepo userRepo;
    UserMapper userMapper;
    VerificationOtpRepo otpRepo;
    PasswordEncoder passwordEncoder;
    EmailService emailService;
    getUserCurrent getUserCurrent;

    //tạo otp
    private String generateOtp() {
        // Thay vì sử dụng Math.random, nên dùng SecureRandom
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1000000));
    }

    //lưu otp vào db
    private void saveOtpForUser(String email, String otp) {
        VerificationOTP verificationOTP = VerificationOTP.builder()
                .otp(otp)
                .email(email)
                .expiryDate(new Date(System.currentTimeMillis() + 120000))
                .build();
        otpRepo.save(verificationOTP);
    }

    //xác thực otp của email đó
    public boolean verifyOtpForRegistration(RegisterRequest request) {
        VerificationOTP verificationOTP = otpRepo.findByOtp(request.getOtp())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));

        if (!verificationOTP.getEmail().equals(request.getEmail())) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        
        if (verificationOTP.getExpiryDate().before(new Date())) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }
        
        return true;
    }

    //gửi otp qua email
    public boolean sendOtp(String email) {
        // Kiểm tra định dạng email
        if (!isValidEmail(email)) {
            throw new AppException(ErrorCode.INVALID_EMAIL_FORMAT);
        }
        
        String otp = generateOtp();
        var existingUser = userRepo.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        
        try {
            emailService.sendOtpEmail(email, otp);
            otpRepo.deleteAllByEmail(email); // Xóa OTP cũ nếu có
            saveOtpForUser(email, otp);
            return true;
        } catch (Exception e) {
            log.error("Failed to send OTP email: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    private boolean isValidEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(regex);
    }

    // xóa otp hết hạn
    @Scheduled(fixedRate = 300000) // 5 phút
    public void deleteExpiredOtp() {
        Date now = new Date();
        try {
            List<VerificationOTP> expiredOtps = otpRepo.findAllByExpiryDateBefore(now);
            if (!expiredOtps.isEmpty()) {
                log.info("Found {} expired OTPs to delete", expiredOtps.size());
                otpRepo.deleteAll(expiredOtps);
                log.info("Successfully deleted expired OTPs");
            }
        } catch (Exception e) {
            log.error("Error deleting expired OTPs: {}", e.getMessage(), e);
        }
    }

    private boolean isPasswordStrong(String password) {
        // Ít nhất 8 ký tự
        if (password.length() < 8) return false;
        
        // Kiểm tra có chữ hoa
        if (!password.matches(".*[A-Z].*")) return false;
        
        // Kiểm tra có chữ thường
        if (!password.matches(".*[a-z].*")) return false;
        
        // Kiểm tra có số
        if (!password.matches(".*\\d.*")) return false;
        
        // Kiểm tra có ký tự đặc biệt
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) return false;
        
        return true;
    }

    @Transactional
    public UserDTO verifyOtpAndSetPassword(RegisterRequest request) throws MessagingException {
        // Kiểm tra độ mạnh của mật khẩu
        if (!isPasswordStrong(request.getPassword())) {
            throw new AppException(ErrorCode.WEAK_PASSWORD); // Cần thêm mã lỗi này
        }
        
        VerificationOTP verificationOTP = otpRepo.findByOtp(request.getOtp())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));

        if (!verificationOTP.getEmail().equals(request.getEmail())) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        var user = userRepo.findByEmail(request.getEmail()).orElseGet(() ->
                Users.builder()
                        .email(request.getEmail())
                        .passwordHash(passwordEncoder.encode(request.getPassword()))
                        .fullName(request.getEmail().split("@")[0]) // Using email prefix as full name initially
                        .role(UserRoleEnum.USER)
                        .salary(BigDecimal.ZERO)
                        .createdAt(new Date())
                        .build()
        );

        userRepo.save(user);
        otpRepo.delete(verificationOTP);
        emailService.sendVerificationEmail(request.getEmail());
        return userMapper.toDTO(user);
    }

}
