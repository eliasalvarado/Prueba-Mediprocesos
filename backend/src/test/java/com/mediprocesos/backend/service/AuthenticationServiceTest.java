package com.mediprocesos.backend.service;

import com.mediprocesos.backend.config.JwtTokenProvider;
import com.mediprocesos.backend.dto.LoginRequestDTO;
import com.mediprocesos.backend.dto.LoginResponseDTO;
import com.mediprocesos.backend.entity.Rol;
import com.mediprocesos.backend.entity.Usuario;
import com.mediprocesos.backend.exception.CredencialesInvalidasException;
import com.mediprocesos.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para AuthenticationService.
 * Prueba las operaciones de login y logout.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationService Tests")
class AuthenticationServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private Usuario usuario;
    private LoginRequestDTO loginRequest;

    @BeforeEach
    void setUp() {
        Rol rol = Rol.builder()
                .id(1)
                .nombre("ADMIN")
                .descripcion("Administrador")
                .build();

        usuario = Usuario.builder()
                .id(1)
                .username("admin")
                .password("$2a$10$hashedPassword")
                .rol(rol)
                .isActive(true)
                .build();

        loginRequest = LoginRequestDTO.builder()
                .username("admin")
                .password("password123")
                .build();
    }

    @Test
    @DisplayName("Login exitoso con credenciales válidas")
    void testLoginExitoso() {
        // Arrange
        when(usuarioRepository.findByUsernameAndIsActiveTrue("admin"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("password123", usuario.getPassword()))
                .thenReturn(true);
        when(jwtTokenProvider.generateToken("admin"))
                .thenReturn("token_jwt_valido");

        // Act
        LoginResponseDTO response = authenticationService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("token_jwt_valido", response.getToken());
        assertEquals("admin", response.getUsuario().getUsername());
        assertEquals("Login exitoso", response.getMensaje());
        
        // Verify
        verify(usuarioRepository, times(1)).findByUsernameAndIsActiveTrue("admin");
        verify(passwordEncoder, times(1)).matches("password123", usuario.getPassword());
        verify(jwtTokenProvider, times(1)).generateToken("admin");
    }

    @Test
    @DisplayName("Login fallido - usuario no encontrado")
    void testLoginUsuarioNoEncontrado() {
        // Arrange
        when(usuarioRepository.findByUsernameAndIsActiveTrue("usuario_inexistente"))
                .thenReturn(Optional.empty());

        LoginRequestDTO loginInvalido = LoginRequestDTO.builder()
                .username("usuario_inexistente")
                .password("password123")
                .build();

        // Act & Assert
        assertThrows(CredencialesInvalidasException.class, () -> {
            authenticationService.login(loginInvalido);
        });

        verify(usuarioRepository, times(1)).findByUsernameAndIsActiveTrue("usuario_inexistente");
    }

    @Test
    @DisplayName("Login fallido - contraseña inválida")
    void testLoginContraseñaInvalida() {
        // Arrange
        when(usuarioRepository.findByUsernameAndIsActiveTrue("admin"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("password_invalida", usuario.getPassword()))
                .thenReturn(false);

        LoginRequestDTO loginInvalido = LoginRequestDTO.builder()
                .username("admin")
                .password("password_invalida")
                .build();

        // Act & Assert
        assertThrows(CredencialesInvalidasException.class, () -> {
            authenticationService.login(loginInvalido);
        });

        verify(passwordEncoder, times(1)).matches("password_invalida", usuario.getPassword());
    }

    @Test
    @DisplayName("Validar token")
    void testValidarToken() {
        // Arrange
        String token = "token_valido";
        when(jwtTokenProvider.validateToken(token)).thenReturn(true);

        // Act
        boolean resultado = authenticationService.validarToken(token);

        // Assert
        assertTrue(resultado);
        verify(jwtTokenProvider, times(1)).validateToken(token);
    }

    @Test
    @DisplayName("Obtener username del token")
    void testObtenerUsernameDelToken() {
        // Arrange
        String token = "token_valido";
        when(jwtTokenProvider.getUsernameFromToken(token)).thenReturn("admin");

        // Act
        String username = authenticationService.obtenerUsernameDelToken(token);

        // Assert
        assertEquals("admin", username);
        verify(jwtTokenProvider, times(1)).getUsernameFromToken(token);
    }

    @Test
    @DisplayName("Logout exitoso")
    void testLogout() {
        // Act
        authenticationService.logout("admin");

        // Assert - No lanza excepción
        verify(usuarioService, never()).obtenerUsuarioPorUsername(anyString());
    }
}
