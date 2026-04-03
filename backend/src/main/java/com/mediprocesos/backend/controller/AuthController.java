package com.mediprocesos.backend.controller;

import com.mediprocesos.backend.dto.LoginRequestDTO;
import com.mediprocesos.backend.dto.LoginResponseDTO;
import com.mediprocesos.backend.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para operaciones de autenticación.
 * Maneja el login y logout de usuarios.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Operaciones de autenticación y autorización")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Realiza el login de un usuario.
     * 
     * @param loginRequest las credenciales de login
     * @return respuesta con token JWT e información del usuario
     */
    @PostMapping("/login")
    @Operation(summary = "Login de usuario", description = "Autentica un usuario y devuelve un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        log.info("POST /api/auth/login - Usuario: {}", loginRequest.getUsername());
        LoginResponseDTO response = authenticationService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Realiza el logout de un usuario.
     * En implementación JWT stateless, es principalmente un endpoint de confirmación.
     * 
     * @return respuesta de logout
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout de usuario", description = "Cierra la sesión del usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout exitoso"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<String> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        log.info("POST /api/auth/logout");
        
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            String username = authenticationService.obtenerUsernameDelToken(jwt);
            authenticationService.logout(username);
        }
        
        return ResponseEntity.ok("Logout exitoso");
    }
}
