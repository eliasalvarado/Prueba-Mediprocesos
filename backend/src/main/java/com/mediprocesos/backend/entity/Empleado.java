package com.mediprocesos.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad Empleado - Representa los datos de los empleados.
 * Relacionada directamente con la entidad Usuario.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
@Entity
@Table(name = "Empleado", indexes = {
    @Index(name = "idx_empleado_numero_identificacion", columnList = "numero_identificacion"),
    @Index(name = "idx_empleado_usuario_id", columnList = "usuario_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empleado {

    /**
     * Identificador único del empleado.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Usuario asociado al empleado (relación uno a uno).
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    /**
     * Primer nombre del empleado.
     */
    @Column(nullable = false, length = 100)
    private String nombre;

    /**
     * Apellidos del empleado.
     */
    @Column(nullable = false, length = 100)
    private String apellidos;

    /**
     * Número de identificación (cédula, pasaporte, etc.).
     */
    @Column(nullable = false, unique = true, length = 50)
    private String numeroIdentificacion;

    /**
     * Fecha de nacimiento del empleado.
     */
    private LocalDate fechaNacimiento;

    /**
     * Género del empleado (M/F/O).
     */
    @Column(length = 1)
    private String genero;

    /**
     * Fecha de creación del registro.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Fecha de última actualización del registro.
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
    }

    /**
     * Hook que se ejecuta antes de actualizar la entidad.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
