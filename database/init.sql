-- SQL Server Initialization Script
-- Prueba Mediprocesos

-- Create Rol table (Lookup table for user roles)
CREATE TABLE Rol (
    id INT PRIMARY KEY IDENTITY(1,1),
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion NVARCHAR(255),
    created_at DATETIME DEFAULT GETDATE()
);

GO

-- Create Usuario table
CREATE TABLE Usuario (
    id INT PRIMARY KEY IDENTITY(1,1),
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol_id INT NOT NULL,
    is_active BIT NOT NULL DEFAULT 1,
    last_login DATETIME NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    CONSTRAINT fk_usuario_rol FOREIGN KEY (rol_id) REFERENCES Rol(id)
);

GO

-- Create Empleado table
CREATE TABLE Empleado (
    id INT PRIMARY KEY IDENTITY(1,1),
    usuario_id INT NOT NULL UNIQUE,
    nombre NVARCHAR(100) NOT NULL,
    apellidos NVARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    genero VARCHAR(10) NOT NULL,
    numero_identificacion VARCHAR(20) NOT NULL UNIQUE,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    CONSTRAINT fk_empleado_usuario FOREIGN KEY (usuario_id) REFERENCES Usuario(id) ON DELETE CASCADE
);

GO

-- Insert default roles
INSERT INTO Rol (nombre, descripcion) VALUES 
('ADMIN', 'Administrador'),
('EMPLEADO', 'Empleado');

GO

-- Insert test users 
-- Password for admin: "admin" hashed with BCrypt (10 rounds)
DECLARE @adminRolId INT;
SELECT @adminRolId = id FROM Rol WHERE nombre = 'ADMIN';

INSERT INTO Usuario (username, password, rol_id, is_active) VALUES 
('admin', '$2a$10$gLoneYgW7afJkVc8lbJ1ieDikZOWyCFNL26ShLtEJRaxl/ppzLVY.', @adminRolId, 1);

GO

-- Insert test employee
DECLARE @adminUserId INT;
SELECT @adminUserId = id FROM Usuario WHERE username = 'admin';

INSERT INTO Empleado (usuario_id, nombre, apellidos, numero_identificacion, fecha_nacimiento, genero) VALUES
(@adminUserId, 'Juan', 'Pérez García', '12345678', '1990-01-15', 'M');

GO

-- Create indexes for better query performance
CREATE INDEX idx_usuario_username ON Usuario(username);
CREATE INDEX idx_usuario_rol_id ON Usuario(rol_id);
CREATE INDEX idx_usuario_is_active ON Usuario(is_active);
CREATE INDEX idx_empleado_numero_identificacion ON Empleado(numero_identificacion);
CREATE INDEX idx_empleado_usuario_id ON Empleado(usuario_id);

