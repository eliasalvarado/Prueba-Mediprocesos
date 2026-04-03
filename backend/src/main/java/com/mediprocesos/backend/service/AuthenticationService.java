package com.mediprocesos.backend.service;

import com.mediprocesos.backend.config.JwtTokenProvider;
import com.mediprocesos.backend.dto.LoginRequestDTO;
import com.mediprocesos.backend.dto.LoginResponseDTO;
import com.mediprocesos.backend.dto.UsuarioDTO;
import com.mediprocesos.backend.entity.Usuario;
import com.mediprocesos.backend.exception.CredencialesInvalidasException;
import com.mediprocesos.backend.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Servicio de autenticación.
 * Maneja el login, logout y validación de tokens JWT.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
@Slf4j
@Service
public class AuthenticationService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Realiza el login de un usuario.
     * 
     * @param loginRequest las credenciales de login
     * @return la respuesta del login con token JWT
     * @throws CredencialesInvalidasException si las credenciales son inválidas
     */
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        log.info("Intentando login para usuario: {}", loginRequest.getUsername());

        // Buscar el usuario
        Usuario usuario = usuarioRepository.findByUsernameAndIsActiveTrue(loginRequest.getUsername())
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado: {}", loginRequest.getUsername());
                    return new CredencialesInvalidasException("Credenciales inválidas");
                });

        // Validar contraseña
        if (!passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
            log.warn("Contraseña inválida para usuario: {}", loginRequest.getUsername());
            throw new CredencialesInvalidasException("Credenciales inválidas");
        }

        // Generar token JWT con el rol del usuario
        String token = jwtTokenProvider.generateToken(usuario.getUsername(), usuario.getRol().getNombre());

        // Actualizar último acceso
        usuarioService.actualizarUltimoAcceso(usuario.getUsername());

        log.info("Login exitoso para usuario: {}", loginRequest.getUsername());

        // Construir respuesta
        UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .isActive(usuario.getIsActive())
                .build();

        return LoginResponseDTO.builder()
                .token(token)
                .usuario(usuarioDTO)
                .mensaje("Login exitoso")
                .build();
    }

    /**
     * Realiza el logout de un usuario.
     * En una implementación con JWT stateless, el logout se maneja del lado del cliente.
     * 
     * @param username el nombre de usuario
     */
    public void logout(String username) {
        log.info("Logout para usuario: {}", username);
        // En implementación stateless con JWT, la lógica de logout es del lado cliente
        // Se puede implementar una lista negra de tokens si es necesario
    }

    /**
     * Valida un token JWT.
     * 
     * @param token el token a validar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validarToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    /**
     * Obtiene el nombre de usuario de un token JWT.
     * 
     * @param token el token JWT
     * @return el nombre de usuario extraído del token
     */
    public String obtenerUsernameDelToken(String token) {
        return jwtTokenProvider.getUsernameFromToken(token);
    }
}
