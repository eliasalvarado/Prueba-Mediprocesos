package com.mediprocesos.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * DTO para la información de Empleado.
 * Usado en transferencias de datos y respuestas CRUD.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Información del empleado")
public class EmpleadoDTO {

    /**
     * ID del empleado.
     */
    @Schema(description = "ID único del empleado", example = "1")
    private Integer id;

    /**
     * Primer nombre del empleado.
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del empleado", example = "Juan")
    private String nombre;

    /**
     * Apellidos del empleado.
     */
    @NotBlank(message = "Los apellidos son obligatorios")
    @Schema(description = "Apellidos del empleado", example = "Pérez García")
    private String apellidos;

    /**
     * Número de identificación único.
     */
    @NotBlank(message = "El número de identificación es obligatorio")
    @Schema(description = "Número de identificación (cédula, pasaporte, etc.)", example = "1234567890")
    private String numeroIdentificacion;

    /**
     * Fecha de nacimiento.
     */
    @Schema(description = "Fecha de nacimiento", example = "1990-05-15")
    private LocalDate fechaNacimiento;

    /**
     * Género del empleado.
     */
    @Schema(description = "Género (M/F/O)", example = "M")
    private String genero;

    /**
     * Username para crear un nuevo usuario (solo al crear empleado).
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "Username para el usuario asociado (solo POST)", example = "jperez")
    private String username;

    /**
     * Password para el nuevo usuario (solo al crear empleado).
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "Password para el usuario asociado (solo POST)", example = "Password123!")
    private String password;

    /**
     * Información del usuario asociado.
     */
    @Schema(description = "Usuario asociado al empleado")
    private UsuarioDTO usuario;
}
