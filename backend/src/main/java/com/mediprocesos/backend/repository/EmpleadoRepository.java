package com.mediprocesos.backend.repository;

import com.mediprocesos.backend.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Empleado.
 * Proporciona métodos CRUD y búsquedas específicas.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    /**
     * Busca empleados por su ID de usuario.
     * 
     * @param usuarioId el ID del usuario
     * @return Optional con el empleado si existe
     */
    Optional<Empleado> findByUsuarioId(Integer usuarioId);

    /**
     * Busca un empleado por su número de identificación.
     * 
     * @param numeroIdentificacion el número de identificación
     * @return Optional con el empleado si existe
     */
    Optional<Empleado> findByNumeroIdentificacion(String numeroIdentificacion);

    /**
     * Busca empleados por el nombre (búsqueda parcial).
     * 
     * @param nombre el nombre del empleado
     * @return lista de empleados que coincidan
     */
    List<Empleado> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca empleados por apellidos (búsqueda parcial).
     * 
     * @param apellidos los apellidos del empleado
     * @return lista de empleados que coincidan
     */
    List<Empleado> findByApellidosContainingIgnoreCase(String apellidos);
}
