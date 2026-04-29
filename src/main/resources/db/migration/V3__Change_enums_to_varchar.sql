-- V3__Change_enums_to_varchar.sql
-- Convert PostgreSQL enum types to VARCHAR for better Hibernate compatibility

-- Drop existing enum constraints and convert to VARCHAR
ALTER TABLE users ALTER COLUMN role TYPE VARCHAR(20);
ALTER TABLE assessments ALTER COLUMN type TYPE VARCHAR(20);
ALTER TABLE results ALTER COLUMN grade TYPE VARCHAR(5);

-- Drop the enum types (they're no longer needed)
DROP TYPE IF EXISTS user_role CASCADE;
DROP TYPE IF EXISTS assessment_type CASCADE;
DROP TYPE IF EXISTS grade_type CASCADE;

-- Add check constraints to maintain data integrity
ALTER TABLE users ADD CONSTRAINT check_user_role CHECK (role IN ('ADMIN', 'STUDENT'));
ALTER TABLE assessments ADD CONSTRAINT check_assessment_type CHECK (type IN ('QUIZ', 'ASSIGNMENT', 'EXAM', 'PROJECT'));
ALTER TABLE results ADD CONSTRAINT check_grade_type CHECK (grade IN ('A+', 'A', 'B+', 'B', 'C+', 'C', 'D', 'F'));
