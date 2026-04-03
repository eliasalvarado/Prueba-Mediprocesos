-- Insert default roles (if not exist)
IF NOT EXISTS (SELECT 1 FROM Rol WHERE nombre = 'ADMIN')
    INSERT INTO Rol (nombre, descripcion, created_at) VALUES ('ADMIN', 'Administrador', GETDATE());

IF NOT EXISTS (SELECT 1 FROM Rol WHERE nombre = 'EMPLEADO')
    INSERT INTO Rol (nombre, descripcion, created_at) VALUES ('EMPLEADO', 'Empleado', GETDATE());

-- Insert test user (if not exist)
DECLARE @adminRolId INT;
SELECT @adminRolId = id FROM Rol WHERE nombre = 'ADMIN';

-- Delete and recreate admin user with correct password hash
DELETE FROM Empleado WHERE usuario_id IN (SELECT id FROM Usuario WHERE username = 'admin');
DELETE FROM Usuario WHERE username = 'admin';

-- Insert admin user with password 'admin'
INSERT INTO Usuario (username, password, rol_id, is_active, created_at, updated_at) VALUES 
('admin', '$2a$10$gLoneYgW7afJkVc8lbJ1ieDikZOWyCFNL26ShLtEJRaxl/ppzLVY.', @adminRolId, 1, GETDATE(), GETDATE());

-- Insert test employee (if not exist)
DECLARE @adminUserId INT;
SELECT @adminUserId = id FROM Usuario WHERE username = 'admin';

IF NOT EXISTS (SELECT 1 FROM Empleado WHERE numero_identificacion = '12345678')
    INSERT INTO Empleado (usuario_id, nombre, apellidos, numero_identificacion, fecha_nacimiento, genero, created_at, updated_at) VALUES
    (@adminUserId, 'Juan', 'Pérez García', '12345678', '1990-01-15', 'M', GETDATE(), GETDATE());
