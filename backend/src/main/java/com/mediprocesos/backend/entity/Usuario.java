package com.mediprocesos.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entidad Usuario - Representa a los usuarios del sistema.
 * Contiene credenciales y referencias a rol y datos de empleado.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
@Entity
@Table(name = "Usuario", indexes = {
    @Index(name = "idx_usuario_username", columnList = "username"),
    @Index(name = "idx_usuario_rol_id", columnList = "rol_id"),
    @Index(name = "idx_usuario_is_active", columnList = "is_active")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    /**
     * Identificador único del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre de usuario único para autenticación.
     */
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * Contraseña del usuario (almacenada con hash).
     */
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * Rol del usuario.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    /**
     * Indica si el usuario está activo.
     */
    @Column(nullable = false)
    private Boolean isActive;

    /**
     * Fecha del último acceso al sistema.
     */
    private LocalDateTime lastLogin;

    /**
     * Fecha de creación del usuario.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Fecha de última actualización del usuario.
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Hook que se ejecuta antes de persistir la entidad.
     */
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        if (isActive == null) {
            isActive = true;
        }
    }

    /**
     * Hook que se ejecuta antes de actualizar la entidad.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
