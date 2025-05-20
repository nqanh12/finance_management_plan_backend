# Finance Manager Plan Backend

Ứng dụng giúp người dùng quản lý tài chính cá nhân bằng cách ghi nhận thu nhập, chi tiêu, tính toán tiết kiệm và đặt mục tiêu tài chính. Hỗ trợ đa nền tảng: Mobile (Android/iOS) và Web.

## 🚀 Tính năng chính
- Ghi nhận thu nhập và chi tiêu
- Tính toán số dư, tiết kiệm
- Đặt và theo dõi mục tiêu tài chính
- Hỗ trợ xác thực OAuth2, gửi email
- Xuất báo cáo (Excel)
- Quản lý qua giao diện Web và API

## 🏗️ Công nghệ sử dụng
- Java 17
- Spring Boot 3.4.3 (Web, Data JPA, Security, Mail, WebSocket, Validation, Thymeleaf)
- PostgreSQL
- MapStruct, Lombok
- Maven

## 📁 Cấu trúc thư mục
```
src/main/java/com/quocanh/finance_manager_plan_backend/
├── config/        # Cấu hình ứng dụng
├── controller/    # REST API controllers
├── dto/           # Data Transfer Objects
├── entity/        # Entity JPA
├── enums/         # Enum dùng chung
├── exception/     # Xử lý ngoại lệ
├── mapper/        # MapStruct mappers
├── repository/    # JPA repositories
├── service/       # Business logic
├── util/          # Tiện ích
└── FinanceManagerPlanBackendApplication.java # Main class
src/main/resources/
├── application.yaml # File cấu hình
├── templates/      # Giao diện Thymeleaf
```

## ⚙️ Cài đặt & Chạy thử
### 1. Clone project
```bash
git clone https://github.com/finance_management_plan_backend/finance_manager_plan_backend.git
cd finance_manager_plan_backend
```
### 2. Cấu hình database & email
- Sửa `src/main/resources/application.yaml` cho phù hợp với môi trường của bạn (PostgreSQL, email SMTP...)

### 3. Build & chạy ứng dụng
```bash
./mvnw spring-boot:run
```
Hoặc build jar:
```bash
./mvnw clean package
java -jar target/finance_manager_plan_backend-0.0.1-SNAPSHOT.jar
```

## 🧪 Kiểm thử
- Test được đặt tại `src/test/java/...`
- Sử dụng Spring Boot Test

## 📄 Tài liệu tham khảo
- [Spring Boot Reference](https://spring.io/projects/spring-boot)
- [Maven Documentation](https://maven.apache.org/guides/index.html)

## 🤝 Đóng góp
Mọi đóng góp, báo lỗi hoặc ý tưởng mới đều được hoan nghênh! Hãy tạo Pull Request hoặc Issue.

---
**Bản quyền © 2024 [Tên của bạn hoặc nhóm]** 
