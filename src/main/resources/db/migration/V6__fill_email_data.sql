INSERT INTO email_data (id, user_id, email)
VALUES
    (1,  1, 'john.doe@example.com'),
    (2,  1, 'j.doe@gmail.com'),
    (3,  2, 'jane.smith@example.com'),
    (4,  3, 'alice.johnson@example.com'),
    (5,  4, 'bob.williams@example.com'),
    (6,  5, 'charlie.brown@example.com'),
    (7,  5, 'cbrown@example.net'),
    (8,  6, 'diana.miller@example.com'),
    (9,  7, 'edward.davis@example.com'),
    (10, 8, 'fiona.garcia@example.com'),
    (11, 8, 'f.garcia@yahoo.com'),
    (12, 9, 'george.martinez@example.com'),
    (13, 10, 'hannah.rodriguez@example.com'),
    (14, 11, 'ivan.lopez@example.com'),
    (15, 12, 'julia.gonzalez@example.com'),
    (16, 13, 'kevin.wilson@example.com'),
    (17, 14, 'laura.anderson@example.com'),
    (18, 15, 'michael.thomas@example.com');

SELECT setval(pg_get_serial_sequence('email_data', 'id'), GREATEST(MAX(id), 0) + 1, false)
FROM email_data;
