DELETE FROM users;
DELETE FROM roles;
INSERT INTO roles (name) VALUES
('ROLE_USER'),
('ROLE_GUEST'),
('ROLE_VIEWER'),
('ROLE_TECHNICIAN')
;

INSERT INTO users (id, handle, email, password) VALUES
(1,'Marcio','teste@teste.com','123');


