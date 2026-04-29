-- V2__Insert_sample_data.sql
-- Sample data for Student Learning Tracker

-- Insert sample users
-- Passwords: admin123 and student123 (BCrypt encoded)
INSERT INTO users (email, password, name, role) VALUES
('admin@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin User', 'ADMIN'),
('student@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'John Doe', 'STUDENT'),
('jane.smith@klu.ac.in', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Jane Smith', 'STUDENT'),
('mike.wilson@klu.ac.in', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Mike Wilson', 'STUDENT'),
('sarah.johnson@klu.ac.in', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Sarah Johnson', 'STUDENT');

-- Insert sample students
INSERT INTO students (user_id, roll_number, department, semester, phone, address) VALUES
((SELECT id FROM users WHERE email = 'student@test.com'), 'CSE2021001', 'Computer Science Engineering', 6, '+91-9876543210', 'Vijayawada, AP'),
((SELECT id FROM users WHERE email = 'jane.smith@klu.ac.in'), 'CSE2021002', 'Computer Science Engineering', 6, '+91-9876543211', 'Guntur, AP'),
((SELECT id FROM users WHERE email = 'mike.wilson@klu.ac.in'), 'CSE2021003', 'Computer Science Engineering', 6, '+91-9876543212', 'Hyderabad, TS'),
((SELECT id FROM users WHERE email = 'sarah.johnson@klu.ac.in'), 'CSE2021004', 'Computer Science Engineering', 6, '+91-9876543213', 'Chennai, TN');

-- Insert sample courses
INSERT INTO courses (code, name, description, credits, instructor_name, instructor_email, semester) VALUES
('CSE301', 'Data Structures and Algorithms', 'Comprehensive study of data structures and algorithmic techniques', 4, 'Dr. Rajesh Kumar', 'rajesh.kumar@klu.ac.in', 3),
('CSE302', 'Database Management Systems', 'Relational databases, SQL, normalization, and transaction management', 3, 'Dr. Priya Sharma', 'priya.sharma@klu.ac.in', 3),
('CSE401', 'Software Engineering', 'Software development lifecycle, design patterns, and project management', 3, 'Dr. Amit Patel', 'amit.patel@klu.ac.in', 4),
('CSE402', 'Computer Networks', 'Network protocols, OSI model, and network security fundamentals', 3, 'Dr. Sunita Reddy', 'sunita.reddy@klu.ac.in', 4),
('CSE501', 'Machine Learning', 'Introduction to ML algorithms, supervised and unsupervised learning', 4, 'Dr. Vikram Singh', 'vikram.singh@klu.ac.in', 5),
('CSE502', 'Web Technologies', 'Full-stack web development with modern frameworks', 3, 'Dr. Meera Gupta', 'meera.gupta@klu.ac.in', 5);

-- Insert sample assessments
INSERT INTO assessments (course_id, title, description, type, total_marks, passing_marks, assessment_date) VALUES
((SELECT id FROM courses WHERE code = 'CSE301'), 'Mid-Term Exam', 'Arrays, Linked Lists, Stacks, and Queues', 'EXAM', 100, 40, '2024-03-15'),
((SELECT id FROM courses WHERE code = 'CSE301'), 'Programming Assignment 1', 'Implement Binary Search Tree operations', 'ASSIGNMENT', 50, 25, '2024-02-20'),
((SELECT id FROM courses WHERE code = 'CSE302'), 'Database Design Project', 'Design and implement a complete database system', 'PROJECT', 100, 50, '2024-04-10'),
((SELECT id FROM courses WHERE code = 'CSE302'), 'SQL Quiz', 'Complex queries, joins, and subqueries', 'QUIZ', 25, 15, '2024-03-05'),
((SELECT id FROM courses WHERE code = 'CSE401'), 'Software Design Assignment', 'Apply design patterns to a real-world problem', 'ASSIGNMENT', 75, 40, '2024-03-25'),
((SELECT id FROM courses WHERE code = 'CSE501'), 'ML Algorithm Implementation', 'Implement and compare classification algorithms', 'PROJECT', 100, 50, '2024-04-15');

-- Insert student course enrollments
INSERT INTO student_course_enrollments (student_id, course_id, enrollment_date) VALUES
((SELECT id FROM students WHERE roll_number = 'CSE2021001'), (SELECT id FROM courses WHERE code = 'CSE301'), '2024-01-15'),
((SELECT id FROM students WHERE roll_number = 'CSE2021001'), (SELECT id FROM courses WHERE code = 'CSE302'), '2024-01-15'),
((SELECT id FROM students WHERE roll_number = 'CSE2021001'), (SELECT id FROM courses WHERE code = 'CSE401'), '2024-01-15'),
((SELECT id FROM students WHERE roll_number = 'CSE2021002'), (SELECT id FROM courses WHERE code = 'CSE301'), '2024-01-15'),
((SELECT id FROM students WHERE roll_number = 'CSE2021002'), (SELECT id FROM courses WHERE code = 'CSE302'), '2024-01-15'),
((SELECT id FROM students WHERE roll_number = 'CSE2021002'), (SELECT id FROM courses WHERE code = 'CSE501'), '2024-01-15'),
((SELECT id FROM students WHERE roll_number = 'CSE2021003'), (SELECT id FROM courses WHERE code = 'CSE401'), '2024-01-15'),
((SELECT id FROM students WHERE roll_number = 'CSE2021003'), (SELECT id FROM courses WHERE code = 'CSE402'), '2024-01-15'),
((SELECT id FROM students WHERE roll_number = 'CSE2021004'), (SELECT id FROM courses WHERE code = 'CSE501'), '2024-01-15'),
((SELECT id FROM students WHERE roll_number = 'CSE2021004'), (SELECT id FROM courses WHERE code = 'CSE502'), '2024-01-15');

-- Insert sample results
INSERT INTO results (student_id, assessment_id, marks_obtained, total_marks, grade, submitted_at) VALUES
((SELECT id FROM students WHERE roll_number = 'CSE2021001'), (SELECT id FROM assessments WHERE title = 'Mid-Term Exam'), 85, 100, 'A', '2024-03-15 14:30:00'),
((SELECT id FROM students WHERE roll_number = 'CSE2021001'), (SELECT id FROM assessments WHERE title = 'Programming Assignment 1'), 42, 50, 'A', '2024-02-20 23:59:00'),
((SELECT id FROM students WHERE roll_number = 'CSE2021001'), (SELECT id FROM assessments WHERE title = 'SQL Quiz'), 22, 25, 'A', '2024-03-05 10:15:00'),
((SELECT id FROM students WHERE roll_number = 'CSE2021002'), (SELECT id FROM assessments WHERE title = 'Mid-Term Exam'), 78, 100, 'B+', '2024-03-15 14:30:00'),
((SELECT id FROM students WHERE roll_number = 'CSE2021002'), (SELECT id FROM assessments WHERE title = 'Programming Assignment 1'), 38, 50, 'B+', '2024-02-20 23:59:00'),
((SELECT id FROM students WHERE roll_number = 'CSE2021002'), (SELECT id FROM assessments WHERE title = 'SQL Quiz'), 20, 25, 'B', '2024-03-05 10:15:00');

-- Insert sample learning progress
INSERT INTO learning_progress (student_id, course_id, session_date, hours_studied, topics_covered, notes, difficulty_level, understanding_level) VALUES
((SELECT id FROM students WHERE roll_number = 'CSE2021001'), (SELECT id FROM courses WHERE code = 'CSE301'), '2024-02-01', 3.5, 'Arrays and Dynamic Arrays', 'Practiced array manipulation problems', 3, 4),
((SELECT id FROM students WHERE roll_number = 'CSE2021001'), (SELECT id FROM courses WHERE code = 'CSE301'), '2024-02-02', 2.0, 'Linked Lists', 'Implemented singly and doubly linked lists', 4, 3),
((SELECT id FROM students WHERE roll_number = 'CSE2021001'), (SELECT id FROM courses WHERE code = 'CSE302'), '2024-02-03', 4.0, 'SQL Basics and Joins', 'Practiced complex join queries', 2, 5),
((SELECT id FROM students WHERE roll_number = 'CSE2021002'), (SELECT id FROM courses WHERE code = 'CSE301'), '2024-02-01', 2.5, 'Stacks and Queues', 'Solved expression evaluation problems', 3, 4),
((SELECT id FROM students WHERE roll_number = 'CSE2021002'), (SELECT id FROM courses WHERE code = 'CSE302'), '2024-02-02', 3.0, 'Database Normalization', 'Studied 1NF, 2NF, and 3NF with examples', 4, 3),
((SELECT id FROM students WHERE roll_number = 'CSE2021003'), (SELECT id FROM courses WHERE code = 'CSE401'), '2024-02-04', 2.5, 'Software Design Patterns', 'Learned Singleton and Factory patterns', 3, 4),
((SELECT id FROM students WHERE roll_number = 'CSE2021004'), (SELECT id FROM courses WHERE code = 'CSE501'), '2024-02-05', 4.5, 'Linear Regression', 'Implemented from scratch using Python', 4, 4);