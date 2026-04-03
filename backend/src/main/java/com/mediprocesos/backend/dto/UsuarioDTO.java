package com.mediprocesos.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para la información de Usuario.
 * Usado en transferencias de datos y respuestas.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Información del usuario")
public class UsuarioDTO {

    /**
     * ID del usuario.
     */
    @Schema(description = "ID único del usuario", example = "1")
    private Integer id;

    /**
     * Nombre de usuario.
     */
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Schema(description = "Nombre de usuario", example = "admin")
    private String username;

    /**
     * Rol del usuario.
     */
    @Schema(description = "Rol del usuario")
    private RolDTO rol;

    /**
     * Indica si el usuario está activo.
     */
    @Schema(description = "Estado del usuario", example = "true")
    private Boolean isActive;
}
