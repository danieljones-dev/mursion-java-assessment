-- Sample data for it_resource table
INSERT INTO resources.it_resource (id, name, type, status, serial_number, inventory_location)
VALUES (uuid_generate_v4(), 'MacBook Pro 2021', 'LAPTOP', 'NOT_ASSIGNED', 'MBP2021-001', 'Warehouse A, Shelf 1'),
       (uuid_generate_v4(), 'Dell XPS 15', 'LAPTOP', 'ASSIGNED', 'DXPS15-001', 'Warehouse B, Shelf 2'),
       (uuid_generate_v4(), 'Herman Miller Aeron Chair', 'CHAIR', 'ASSIGNED', 'HMAC-001', 'Warehouse A, Section C'),
       (uuid_generate_v4(), 'iPhone 13', 'MOBILE', 'NOT_ASSIGNED', 'IP13-001', 'Warehouse B, Secure Cabinet 1'),
       (uuid_generate_v4(), 'Logitech MX Master 3', 'MOUSE', 'NOT_ASSIGNED', 'LMM3-001', 'Warehouse A, Shelf 3');

-- Sample data for it_resource_assign table
INSERT INTO resources.it_resource_assign (id, resource_id, user_id, status, assigned_date, return_date)
VALUES (uuid_generate_v4(),
        (SELECT id FROM resources.it_resource WHERE name = 'Dell XPS 15'),
        uuid_generate_v4(),
        'NOT_RETURNED',
        '2023-01-15',
        NULL),
       (uuid_generate_v4(),
        (SELECT id FROM resources.it_resource WHERE name = 'Herman Miller Aeron Chair'),
        uuid_generate_v4(),
        'NOT_RETURNED',
        '2023-02-20',
        NULL);