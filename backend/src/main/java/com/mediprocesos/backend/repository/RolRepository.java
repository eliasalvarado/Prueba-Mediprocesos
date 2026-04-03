package com.mediprocesos.backend.repository;

import com.mediprocesos.backend.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio para la entidad Rol.
 * Proporciona métodos CRUD y búsquedas específicas.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

    /**
     * Busca un rol por su nombre.
     * 
     * @param nombre el nombre del rol
     * @return Optional con el rol si existe
     */
    Optional<Rol> findByNombre(String nombre);
}
