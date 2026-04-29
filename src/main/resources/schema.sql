-- Minimal schema for H2 before data.sql
CREATE TABLE IF NOT EXISTS users (
  id IDENTITY PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  role VARCHAR(20) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS students (
  id IDENTITY PRIMARY KEY,
  user_id BIGINT REFERENCES users(id),
  roll_number VARCHAR(50),
  department VARCHAR(100),
  semester INT
);

CREATE TABLE IF NOT EXISTS courses (
  id IDENTITY PRIMARY KEY,
  code VARCHAR(20) NOT NULL UNIQUE,
  name VARCHAR(255) NOT NULL,
  credits INT,
  semester INT
);

CREATE TABLE IF NOT EXISTS student_course_enrollments (
  id IDENTITY PRIMARY KEY,
  student_id BIGINT REFERENCES students(id),
  course_id BIGINT REFERENCES courses(id)
);
