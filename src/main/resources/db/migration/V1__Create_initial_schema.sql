-- V1__Create_initial_schema.sql
-- Student Learning Tracker Database Schema

-- Create ENUM types
CREATE TYPE user_role AS ENUM ('ADMIN', 'STUDENT');
CREATE TYPE assessment_type AS ENUM ('QUIZ', 'ASSIGNMENT', 'EXAM', 'PROJECT');
CREATE TYPE grade_type AS ENUM ('A+', 'A', 'B+', 'B', 'C+', 'C', 'D', 'F');

-- Users table (for authentication)
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    role user_role NOT NULL DEFAULT 'STUDENT',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Students table (extends users for student-specific data)
CREATE TABLE students (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    roll_number VARCHAR(50) UNIQUE,
    department VARCHAR(100),
    semester INTEGER CHECK (semester >= 1 AND semester <= 8),
    phone VARCHAR(20),
    address TEXT,
    date_of_birth DATE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Courses table
CREATE TABLE courses (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    credits INTEGER NOT NULL DEFAULT 3 CHECK (credits > 0),
    instructor_name VARCHAR(255),
    instructor_email VARCHAR(255),
    semester INTEGER CHECK (semester >= 1 AND semester <= 8),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Assessments table
CREATE TABLE assessments (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    type assessment_type NOT NULL,
    total_marks INTEGER NOT NULL CHECK (total_marks > 0),
    passing_marks INTEGER NOT NULL CHECK (passing_marks >= 0),
    assessment_date DATE,
    duration_minutes INTEGER,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT passing_marks_check CHECK (passing_marks <= total_marks)
);

-- Results table (student assessment results)
CREATE TABLE results (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    assessment_id BIGINT NOT NULL REFERENCES assessments(id) ON DELETE CASCADE,
    marks_obtained INTEGER NOT NULL CHECK (marks_obtained >= 0),
    total_marks INTEGER NOT NULL CHECK (total_marks > 0),
    percentage DECIMAL(5,2) GENERATED ALWAYS AS (
        CASE 
            WHEN total_marks > 0 THEN (marks_obtained * 100.0 / total_marks)
            ELSE 0
        END
    ) STORED,
    grade grade_type,
    remarks TEXT,
    submitted_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(student_id, assessment_id),
    CONSTRAINT marks_obtained_check CHECK (marks_obtained <= total_marks)
);

-- Learning Progress table (study sessions and progress tracking)
CREATE TABLE learning_progress (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    course_id BIGINT NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    session_date DATE NOT NULL,
    hours_studied DECIMAL(4,2) NOT NULL CHECK (hours_studied >= 0),
    topics_covered TEXT,
    notes TEXT,
    difficulty_level INTEGER CHECK (difficulty_level >= 1 AND difficulty_level <= 5),
    understanding_level INTEGER CHECK (understanding_level >= 1 AND understanding_level <= 5),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Student Course Enrollment table (many-to-many relationship)
CREATE TABLE student_course_enrollments (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    course_id BIGINT NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    enrollment_date DATE NOT NULL DEFAULT CURRENT_DATE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(student_id, course_id)
);

-- Create indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_students_user_id ON students(user_id);
CREATE INDEX idx_students_roll_number ON students(roll_number);
CREATE INDEX idx_students_department ON students(department);
CREATE INDEX idx_courses_code ON courses(code);
CREATE INDEX idx_courses_semester ON courses(semester);
CREATE INDEX idx_courses_active ON courses(is_active);
CREATE INDEX idx_assessments_course_id ON assessments(course_id);
CREATE INDEX idx_assessments_type ON assessments(type);
CREATE INDEX idx_assessments_date ON assessments(assessment_date);
CREATE INDEX idx_results_student_id ON results(student_id);
CREATE INDEX idx_results_assessment_id ON results(assessment_id);
CREATE INDEX idx_results_grade ON results(grade);
CREATE INDEX idx_learning_progress_student_id ON learning_progress(student_id);
CREATE INDEX idx_learning_progress_course_id ON learning_progress(course_id);
CREATE INDEX idx_learning_progress_date ON learning_progress(session_date);
CREATE INDEX idx_enrollments_student_id ON student_course_enrollments(student_id);
CREATE INDEX idx_enrollments_course_id ON student_course_enrollments(course_id);
CREATE INDEX idx_enrollments_active ON student_course_enrollments(is_active);

-- Create function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers to automatically update updated_at
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_students_updated_at BEFORE UPDATE ON students FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_courses_updated_at BEFORE UPDATE ON courses FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_assessments_updated_at BEFORE UPDATE ON assessments FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_results_updated_at BEFORE UPDATE ON results FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_learning_progress_updated_at BEFORE UPDATE ON learning_progress FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();