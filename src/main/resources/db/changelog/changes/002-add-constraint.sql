ALTER TABLE resources.it_resource
    ADD CONSTRAINT check_it_resource_status
        CHECK (status IN ('ASSIGNED', 'NOT_ASSIGNED', 'DEPRECATED'));

ALTER TABLE resources.it_resource_assign
    ADD CONSTRAINT check_it_resource_assign_status
        CHECK (status IN ('RETURNED', 'NOT_RETURNED'));


ALTER TABLE resources.it_resource
    ADD CONSTRAINT uk_it_resource_serial_number UNIQUE (serial_number);