ALTER TABLE resources.it_resource
    ADD CONSTRAINT check_it_resource_type
        CHECK (type IN
               ('LAPTOP', 'CHAIR', 'MOUSE', 'MOBILE', 'KEYBOARD', 'MONITOR', 'DESKTOP', 'PRINTER', 'SCANNER', 'OTHER'));