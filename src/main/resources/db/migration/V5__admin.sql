-- V5__admin.sql
INSERT INTO usuario (username, password, role, enabled)
VALUES ('admin',
        '$2a$10$Wb3v8x4l5iN7Z8wG3hI0Ye3r0dX0k2cC0o7o6aQXfWm9wV9i8bq9a', -- bcrypt de 'admin123' (exemplo)
        'ROLE_MANAGER',
        'S'); -- se sua coluna enabled for CHAR(1) S/N; se for boolean, use TRUE
