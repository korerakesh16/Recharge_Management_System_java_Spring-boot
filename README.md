# Lumen Project - Simplified

A simple Spring Boot REST API for user management with login and registration functionality.

## Features

- User registration
- User login
- Simple REST API endpoints
- MySQL database integration

## Project Structure

```
src/
├── main/
│   ├── java/com/dinesh/lumen_project_1/
│   │   ├── LumenProject1Application.java    # Main application class
│   │   ├── controller/
│   │   │   └── UserController.java          # Single controller for all endpoints
│   │   ├── model/
│   │   │   └── User.java                    # User entity
│   │   ├── repository/
│   │   │   └── UserRepository.java          # Data access layer
│   │   └── service/
│   │       └── UserService.java             # Business logic
│   └── resources/
│       └── application.properties           # Database configuration
```

## API Endpoints

### Base URL: `http://localhost:8080/api`

- **GET** `/` - Welcome message
- **POST** `/login` - User login
- **POST** `/signup` - User registration

### Request/Response Format

All endpoints use JSON format with the User object:

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "1234567890",
  "address": "123 Main St",
  "password": "password123"
}
```

## Database Setup

1. Create MySQL database: `lumenproject1`
2. Update credentials in `application.properties` if needed
3. Tables will be created automatically on first run

## Running the Application

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## Simplifications Made

- Eliminated separate DTOs - uses User entity directly
- Combined controllers into single UserController
- Simplified User model with cleaner structure
- Reduced boilerplate code throughout
- Fixed ID type consistency (Long instead of Integer)
- Streamlined application.properties
- Removed unnecessary complexity while maintaining functionality
