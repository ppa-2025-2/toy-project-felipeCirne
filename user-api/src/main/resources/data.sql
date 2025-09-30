DELETE FROM users;
DELETE FROM roles;
DELETE FROM tickets;
INSERT INTO roles (name) VALUES
('ROLE_USER'),
('ROLE_GUEST'),
('ROLE_VIEWER'),
('ROLE_TECHNICIAN')
;

INSERT INTO users (id, handle, email, password) VALUES
(1,'Marcio','teste@teste.com','123');


INSERT INTO tickets (owner_id, recipient_id, object, action, details, local) VALUES

(1,
 1, 
 '123',
 '123', 
 '123', 
 '123');