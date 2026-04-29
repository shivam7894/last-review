-- Sample data for H2 in-memory database
-- BCrypt for 'admin123' and 'student123': $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

INSERT INTO users (email, password, name, role, created_at) VALUES
('admin@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin User', 'ADMIN', CURRENT_TIMESTAMP),
('student@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'John Doe', 'STUDENT', CURRENT_TIMESTAMP);

INSERT INTO students (user_id, roll_number, department, semester) VALUES
((SELECT id FROM users WHERE email = 'student@test.com'), 'CSE2021001', 'Computer Science', 6);

INSERT INTO courses (code, name, credits, semester) VALUES 
('CSE301', 'Data Structures', 4, 6),
('CSE302', 'Databases', 3, 6);

INSERT INTO student_course_enrollments (student_id, course_id) VALUES
((SELECT id FROM students WHERE roll_number = 'CSE2021001'), (SELECT id FROM courses WHERE code = 'CSE301'));

-- Verify data
SELECT 'Data loaded successfully' as status;

