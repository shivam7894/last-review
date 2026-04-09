# Student Learning Tracker - Spring Boot Backend

## Overview
This is the Spring Boot backend for the Student Learning Tracker system, replacing the Node.js backend while maintaining the same API structure.

## Tech Stack
- **Java 17**
- **Spring Boot 3.5.13**
- **Spring Security** with JWT authentication
- **MongoDB** for database
- **Lombok** for reducing boilerplate code
- **Maven** for dependency management

## Project Structure
```
src/main/java/com/tracker/
├── config/              # Security and global configuration
├── controller/          # REST API controllers
├── dto/                 # Data Transfer Objects
├── model/              # MongoDB entities
├── repository/         # MongoDB repositories
├── security/           # JWT utilities and filters
└── service/            # Business logic layer
```

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MongoDB running on localhost:27017

### Configuration
Update `src/main/resources/application.properties`:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/student_tracker
jwt.secret=your-secret-key-change-this-in-production-minimum-256-bits
jwt.expiration=86400000
server.port=8080
```

### Build and Run

1. **Build the project:**
```bash
mvn clean install
```

2. **Run the application:**
```bash
mvn spring-boot:run
```

Or run directly:
```bash
java -jar target/student-tracker-0.0.1-SNAPSHOT.jar
```

The server will start on `http://localhost:8080`

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login

### Students (Admin only for POST/PUT/DELETE)
- `GET /api/students` - Get all students
- `GET /api/students/{id}` - Get student by ID
- `POST /api/students` - Create student
- `PUT /api/students/{id}` - Update student
- `DELETE /api/students/{id}` - Delete student

### Courses (Admin only for POST)
- `GET /api/courses` - Get all courses
- `GET /api/courses/{id}` - Get course by ID
- `POST /api/courses` - Create course

### Assessments (Admin only for POST)
- `GET /api/assessments` - Get all assessments
- `GET /api/assessments/{id}` - Get assessment by ID
- `POST /api/assessments` - Create assessment

### Results (Admin only for POST)
- `GET /api/results` - Get all results
- `POST /api/results` - Create result
- `GET /api/results/student/{studentId}` - Get student results

### Reports
- `GET /api/reports/performance/{studentId}` - Get performance report

## Authentication
All endpoints except `/api/auth/**` require JWT token in the Authorization header:
```
Authorization: Bearer <token>
```

## Models

### User
- name, email, password, role (ADMIN/STUDENT)

### Student
- studentId (auto-generated), name, email, courses[]

### Course
- courseId (auto-generated), courseName, instructor, description, students[]

### Assessment
- assessmentId (auto-generated), courseId, title, assessmentType (QUIZ/ASSIGNMENT/EXAM/PROJECT), maxMarks, date, description

### Result
- studentId, assessmentId, marksObtained, percentage (auto-calculated), grade (auto-calculated)

## Frontend Integration
Update your frontend API base URL to:
```javascript
const API_BASE_URL = 'http://localhost:8080/api';
```

The API structure remains the same as the Node.js version, so minimal frontend changes are needed.

## Development
- Hot reload: Use Spring Boot DevTools (already included)
- Testing: Run `mvn test`
- Package: Run `mvn package`
"# student-learn-backend" 
