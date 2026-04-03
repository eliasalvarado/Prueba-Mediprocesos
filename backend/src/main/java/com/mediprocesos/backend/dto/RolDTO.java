package com.mediprocesos.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para la información de Rol.
 * Usado en transferencias de datos.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Información del rol")
public class RolDTO {

    /**
     * ID del rol.
     */
    @Schema(description = "ID único del rol", example = "1")
    private Integer id;

    /**
     * Nombre del rol.
     */
    @NotBlank(message = "El nombre del rol es obligatorio")
    @Schema(description = "Nombre del rol", example = "ADMIN")
    private String nombre;

    /**
     * Descripción del rol.
     */
    @Schema(description = "Descripción del rol", example = "Administrador del sistema")
    private String descripcion;
}
