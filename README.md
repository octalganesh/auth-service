# FSM Auth Service

A Spring Boot microservice for authentication and authorization in the Field Service Management (FSM) SaaS platform.
This service handles JWT-based authentication, user management, and integrates with Eureka service discovery.

## 🚀 Features

- **JWT Authentication**: Access and refresh token generation with configurable validity
- **User Login/Logout**: Secure authentication with email and password
- **Password Management**: Change password functionality
- **Role-Based Access Control**: User roles and permissions management
- **Service Discovery**: Eureka client integration for microservices architecture
- **Multi-Environment Support**: Dev, staging, and production configurations
- **API Documentation**: OpenAPI/Swagger integration
- **Cross-Origin Support**: CORS configuration for web clients
- **Database Integration**: MySQL with JPA/Hibernate
- **MapStruct Integration**: Object mapping utilities

## 🛠️ Tech Stack

- **Framework**: Spring Boot 2.3.12
- **Java Version**: 11
- **Database**: MySQL 5.7+
- **ORM**: Spring Data JPA with Hibernate
- **Security**: Spring Security with JWT
- **Service Discovery**: Netflix Eureka Client
- **Documentation**: OpenAPI 3 (Swagger)
- **Build Tool**: Maven
- **Object Mapping**: MapStruct 1.4.2

## 📁 Project Structure

```
src/main/java/com/octal/fsm/
├── AuthApplication.java              # Main Spring Boot application class
├── common/                          # Common utilities and API response models
│   ├── ApiResponse.java
│   └── CommonConstants.java
├── configuration/                   # Configuration classes
│   ├── OpenApiConfig.java          # Swagger/OpenAPI configuration
│   └── WebSecurityConfig.java      # Security configuration
├── controller/                      # REST API controllers
│   ├── AuthController.java         # Authentication endpoints
│   ├── BaseController.java         # Base controller utilities
│   └── UserController.java         # User management endpoints
├── dto/                            # Data Transfer Objects
│   ├── AuthenticationResponse.java
│   ├── AuthUserDTO.java
│   ├── ChangePasswordDTO.java
│   ├── LoginRequest.java
│   └── UserDTO.java
├── entities/                       # JPA entities
│   ├── AbstractPersistable.java   # Base entity class
│   ├── Role.java                   # User roles
│   └── User.java                   # User entity
├── exceptions/                     # Custom exception handling
├── jwt/                           # JWT token management
│   ├── AuthorizationFilter.java   # JWT authorization filter
│   ├── CustomUserDetailsService.java
│   └── JwtTokenProvider.java      # JWT token generation/validation
├── repositories/                   # Spring Data repositories
│   └── UserRepository.java
├── service/                       # Business logic services
│   ├── UserService.java
│   └── impl/UserServiceImpl.java
└── utils/                         # Utility classes
    └── TextUtils.java
```

## ⚙️ Configuration

### Environment Profiles

The application supports multiple environments with specific configurations:

- **Development** (`dev`): Port 8086, local MySQL database
- **Staging** (`staging`): Staging environment configurations
- **Production** (`prod`): Production environment configurations

### JWT Configuration

```yaml
jwt:
  access:
    token:
      validity: 7776000  # Token validity in seconds (90 days)
  refresh:
    token:
      validity: 7776000  # Refresh token validity in seconds
  secret: fsm            # JWT signing secret
```

### Database Configuration (Development)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fsm-auth
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
```

## 🚀 Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6+
- MySQL 5.7+
- Eureka Server (for service discovery)

### Installation & Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd fsm-auth-service
   ```

2. **Setup MySQL Database**
   ```sql
   CREATE DATABASE `fsm-auth`;
   ```

3. **Configure Environment**
    - Update database credentials in `application-dev.yaml`
    - Configure Eureka server URL if different from default

4. **Build the Project**
   ```bash
   mvn clean install
   ```

5. **Run the Application**
   ```bash
   # Run with development profile (default)
   mvn spring-boot:run
   
   # Or run with specific profile
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

## 📚 API Endpoints

### Authentication Endpoints

- **POST** `/auth/login` - User login
- **GET** `/auth/details/by/email/{email}` - Get user details by email

### User Management Endpoints

- **Base URL**: `/user`
- Additional endpoints available in UserController

## 📖 API Documentation

Once the application is running, access the Swagger UI at:

```
http://localhost:8086/swagger-ui.html
```

## 🔧 Service Discovery

This service integrates with Netflix Eureka for service discovery. Configure the Eureka server URL:

```yaml
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
```

## 🏗️ Build & Deployment

### Maven Build

```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package application
mvn package

# Build Docker image (if Dockerfile available)
docker build -t fsm-auth-service .
```

### JAR Execution

```bash
java -jar target/auth-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## 🔐 Security

- JWT tokens are used for stateless authentication
- Passwords are securely hashed using Spring Security
- CORS is configured for cross-origin requests
- Role-based access control for API endpoints

## 🤝 Contributing

This is a proprietary project for the FSM SaaS platform. Please follow the established coding standards and submit pull
requests for review.

## 📝 License

This project is proprietary and confidential. All rights reserved.
