package com.mediprocesos.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entidad Rol - Representa los roles de usuario en el sistema.
 * Define los permisos y funcionalidades disponibles para cada rol.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
@Entity
@Table(name = "Rol")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rol {

    /**
     * Identificador único del rol.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre único del rol (ejemplo: ADMIN, EMPLEADO).
     */
    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    /**
     * Descripción del rol.
     */
    @Column(length = 255)
    private String descripcion;

    /**
     * Fecha de creación del registro.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Hook que se ejecuta antes de persistir la entidad.
     */
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
