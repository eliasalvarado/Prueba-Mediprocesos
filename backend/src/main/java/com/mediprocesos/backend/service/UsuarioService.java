package com.mediprocesos.backend.service;

import com.mediprocesos.backend.dto.UsuarioDTO;
import com.mediprocesos.backend.entity.Usuario;
import com.mediprocesos.backend.exception.UsuarioNoEncontradoException;
import com.mediprocesos.backend.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para operaciones CRUD de Usuario.
 * Maneja la lógica de negocio relacionada con usuarios.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
@Slf4j
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Obtiene todos los usuarios.
     * 
     * @return lista de todos los usuarios
     */
    public List<Usuario> obtenerTodosLosUsuarios() {
        log.info("Obteniendo todos los usuarios");
        return usuarioRepository.findAll();
    }

    /**
     * Obtiene un usuario por su ID.
     * 
     * @param id el ID del usuario
     * @return el usuario encontrado
     * @throws UsuarioNoEncontradoException si el usuario no existe
     */
    public Usuario obtenerUsuarioPorId(Integer id) {
        log.info("Obteniendo usuario por ID: {}", id);
        return usuarioRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID " + id + " no encontrado"));
    }

    /**
     * Obtiene un usuario por su nombre de usuario.
     * 
     * @param username el nombre de usuario
     * @return Optional con el usuario si existe
     */
    public Optional<Usuario> obtenerUsuarioPorUsername(String username) {
        log.info("Obteniendo usuario por username: {}", username);
        return usuarioRepository.findByUsername(username);
    }

    /**
     * Crea un nuevo usuario.
     * 
     * @param usuario el usuario a crear
     * @return el usuario creado
     */
    public Usuario crearUsuario(Usuario usuario) {
        log.info("Creando nuevo usuario: {}", usuario.getUsername());
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setIsActive(true);
        return usuarioRepository.save(usuario);
    }

    /**
     * Actualiza un usuario existente.
     * 
     * @param id el ID del usuario a actualizar
     * @param usuarioActualizado los nuevos datos del usuario
     * @return el usuario actualizado
     * @throws UsuarioNoEncontradoException si el usuario no existe
     */
    public Usuario actualizarUsuario(Integer id, Usuario usuarioActualizado) {
        log.info("Actualizando usuario con ID: {}", id);
        Usuario usuario = obtenerUsuarioPorId(id);
        
        if (usuarioActualizado.getUsername() != null) {
            usuario.setUsername(usuarioActualizado.getUsername());
        }
        if (usuarioActualizado.getRol() != null) {
            usuario.setRol(usuarioActualizado.getRol());
        }
        if (usuarioActualizado.getIsActive() != null) {
            usuario.setIsActive(usuarioActualizado.getIsActive());
        }
        
        usuario.setUpdatedAt(LocalDateTime.now());
        return usuarioRepository.save(usuario);
    }

    /**
     * Desactiva un usuario.
     * 
     * @param id el ID del usuario a desactivar
     * @throws UsuarioNoEncontradoException si el usuario no existe
     */
    public void desactivarUsuario(Integer id) {
        log.info("Desactivando usuario con ID: {}", id);
        Usuario usuario = obtenerUsuarioPorId(id);
        usuario.setIsActive(false);
        usuarioRepository.save(usuario);
    }

    /**
     * Actualiza el último acceso del usuario.
     * 
     * @param username el nombre de usuario
     */
    public void actualizarUltimoAcceso(String username) {
        log.info("Actualizando último acceso del usuario: {}", username);
        Optional<Usuario> usuario = obtenerUsuarioPorUsername(username);
        usuario.ifPresent(u -> {
            u.setLastLogin(LocalDateTime.now());
            usuarioRepository.save(u);
        });
    }

    /**
     * Convierte un Usuario a UsuarioDTO.
     * 
     * @param usuario el usuario a convertir
     * @return el DTO del usuario
     */
    public UsuarioDTO convertirADTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .isActive(usuario.getIsActive())
                .rol(null) // Se establecería el RolDTO aquí si fuera necesario
                .build();
    }
}
