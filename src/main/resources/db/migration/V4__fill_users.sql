INSERT INTO users (id, name, date_of_birth, password)
VALUES
    (1,  'John Doe',         '01.01.1990', 'secret321'),
    (2,  'Jane Smith',       '15.05.1985', 'secret123'),
    (3,  'Alice Johnson',    '03.03.1992', 'alicePass'),
    (4,  'Bob Williams',     '28.07.1988', 'bobSecure'),
    (5,  'Charlie Brown',    '12.12.1975', 'charlie123'),
    (6,  'Diana Miller',     '30.09.1995', 'diana2020'),
    (7,  'Edward Davis',     '22.02.1983', 'edward!%$'),
    (8,  'Fiona Garcia',     '09.10.1991', 'fiona2021'),
    (9,  'George Martinez',  '17.04.1980', 'geo&M'),
    (10, 'Hannah Rodriguez', '25.11.1987', 'hannahPass'),
    (11, 'Ivan Lopez',       '05.05.1993', 'ivan007'),
    (12, 'Julia Gonzalez',   '14.08.1989', 'jul1a!'),
    (13, 'Kevin Wilson',     '06.06.1984', 'kenw_secure'),
    (14, 'Laura Anderson',   '19.03.1997', 'laura#99'),
    (15, 'Michael Thomas',   '31.12.1982', 'mike$22');

SELECT setval(pg_get_serial_sequence('users', 'id'), GREATEST(MAX(id), 0) + 1, false)
FROM users;
