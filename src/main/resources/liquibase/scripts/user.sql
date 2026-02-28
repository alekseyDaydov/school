-- liquibase formatted sql

-- changeset a.davydov:1
CREATE INDEX idx_student_name ON student (name)

-- changeset a.davydov:2
CREATE INDEX idx_faculty_name_color ON faculty (name, color)