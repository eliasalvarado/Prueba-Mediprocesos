package com.mediprocesos.backend.repository;

import com.mediprocesos.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 * Proporciona métodos CRUD y búsquedas específicas.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Busca un usuario por su nombre de usuario.
     * 
     * @param username el nombre de usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Busca un usuario activo por su ID.
     * 
     * @param id el ID del usuario
     * @return Optional con el usuario si existe y está activo
     */
    Optional<Usuario> findByIdAndIsActiveTrue(Integer id);

    /**
     * Busca un usuario activo por su nombre de usuario.
     * 
     * @param username el nombre de usuario
     * @return Optional con el usuario si existe y está activo
     */
    Optional<Usuario> findByUsernameAndIsActiveTrue(String username);
}
