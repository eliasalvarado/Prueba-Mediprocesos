package com.mediprocesos.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta del login.
 * Contiene el token JWT y la información del usuario.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta de login con token JWT")
public class LoginResponseDTO {

    /**
     * Token JWT generado.
     */
    @Schema(description = "Token JWT para autenticación", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    /**
     * Información del usuario autenticado.
     */
    @Schema(description = "Información del usuario autenticado")
    private UsuarioDTO usuario;

    /**
     * Mensaje de éxito.
     */
    @Schema(description = "Mensaje de respuesta", example = "Login exitoso")
    private String mensaje;
}
