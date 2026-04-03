package com.mediprocesos.backend.service;

import com.mediprocesos.backend.dto.EmpleadoDTO;
import com.mediprocesos.backend.dto.UsuarioDTO;
import com.mediprocesos.backend.entity.Empleado;
import com.mediprocesos.backend.exception.EmpleadoDuplicadoException;
import com.mediprocesos.backend.exception.UsuarioNoEncontradoException;
import com.mediprocesos.backend.repository.EmpleadoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para operaciones CRUD de Empleado.
 * Maneja la lógica de negocio relacionada con empleados.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
@Slf4j
@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    /**
     * Obtiene todos los empleados.
     * 
     * @return lista de todos los empleados
     */
    public List<Empleado> obtenerTodosLosEmpleados() {
        log.info("Obteniendo todos los empleados");
        return empleadoRepository.findAll();
    }

    /**
     * Obtiene un empleado por su ID.
     * 
     * @param id el ID del empleado
     * @return el empleado encontrado
     * @throws UsuarioNoEncontradoException si el empleado no existe
     */
    public Empleado obtenerEmpleadoPorId(Integer id) {
        log.info("Obteniendo empleado por ID: {}", id);
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Empleado con ID " + id + " no encontrado"));
    }

    /**
     * Obtiene un empleado por su número de identificación.
     * 
     * @param numeroIdentificacion el número de identificación
     * @return el empleado encontrado
     * @throws UsuarioNoEncontradoException si el empleado no existe
     */
    public Empleado obtenerEmpleadoPorNumeroIdentificacion(String numeroIdentificacion) {
        log.info("Obteniendo empleado por número de identificación: {}", numeroIdentificacion);
        return empleadoRepository.findByNumeroIdentificacion(numeroIdentificacion)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Empleado con identificación " + numeroIdentificacion + " no encontrado"));
    }

    /**
     * Busca empleados por nombre.
     * 
     * @param nombre el nombre a buscar
     * @return lista de empleados que coinciden
     */
    public List<Empleado> buscarEmpleadosPorNombre(String nombre) {
        log.info("Buscando empleados por nombre: {}", nombre);
        return empleadoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Busca empleados por apellidos.
     * 
     * @param apellidos los apellidos a buscar
     * @return lista de empleados que coinciden
     */
    public List<Empleado> buscarEmpleadosPorApellidos(String apellidos) {
        log.info("Buscando empleados por apellidos: {}", apellidos);
        return empleadoRepository.findByApellidosContainingIgnoreCase(apellidos);
    }

    /**
     * Crea un nuevo empleado.
     * 
     * @param empleado el empleado a crear
     * @return el empleado creado
     * @throws EmpleadoDuplicadoException si el número de identificación ya existe
     */
    public Empleado crearEmpleado(Empleado empleado) {
        log.info("Creando nuevo empleado: {}", empleado.getNombre());
        
        // Verificar si el número de identificación ya existe
        empleadoRepository.findByNumeroIdentificacion(empleado.getNumeroIdentificacion())
                .ifPresent(e -> {
                    throw new EmpleadoDuplicadoException("Ya existe un empleado con el número de identificación " + empleado.getNumeroIdentificacion());
                });

        return empleadoRepository.save(empleado);
    }

    /**
     * Actualiza un empleado existente.
     * 
     * @param id el ID del empleado a actualizar
     * @param empleadoActualizado los nuevos datos del empleado
     * @return el empleado actualizado
     * @throws UsuarioNoEncontradoException si el empleado no existe
     */
    public Empleado actualizarEmpleado(Integer id, Empleado empleadoActualizado) {
        log.info("Actualizando empleado con ID: {}", id);
        
        Empleado empleado = obtenerEmpleadoPorId(id);
        
        if (empleadoActualizado.getNombre() != null) {
            empleado.setNombre(empleadoActualizado.getNombre());
        }
        if (empleadoActualizado.getApellidos() != null) {
            empleado.setApellidos(empleadoActualizado.getApellidos());
        }
        if (empleadoActualizado.getFechaNacimiento() != null) {
            empleado.setFechaNacimiento(empleadoActualizado.getFechaNacimiento());
        }
        if (empleadoActualizado.getGenero() != null) {
            empleado.setGenero(empleadoActualizado.getGenero());
        }
        
        empleado.setUpdatedAt(LocalDateTime.now());
        return empleadoRepository.save(empleado);
    }

    /**
     * Elimina un empleado por su ID.
     * 
     * @param id el ID del empleado a eliminar
     * @throws UsuarioNoEncontradoException si el empleado no existe
     */
    public void eliminarEmpleado(Integer id) {
        log.info("Eliminando empleado con ID: {}", id);
        
        Empleado empleado = obtenerEmpleadoPorId(id);
        empleadoRepository.delete(empleado);
    }

    /**
     * Convierte un Empleado a EmpleadoDTO.
     * 
     * @param empleado el empleado a convertir
     * @return el DTO del empleado
     */
    public EmpleadoDTO convertirADTO(Empleado empleado) {
        UsuarioDTO usuarioDTO = null;
        if (empleado.getUsuario() != null) {
            usuarioDTO = UsuarioDTO.builder()
                    .id(empleado.getUsuario().getId())
                    .username(empleado.getUsuario().getUsername())
                    .isActive(empleado.getUsuario().getIsActive())
                    .build();
        }
        
        return EmpleadoDTO.builder()
                .id(empleado.getId())
                .nombre(empleado.getNombre())
                .apellidos(empleado.getApellidos())
                .numeroIdentificacion(empleado.getNumeroIdentificacion())
                .fechaNacimiento(empleado.getFechaNacimiento())
                .genero(empleado.getGenero())
                .usuario(usuarioDTO)
                .build();
    }
}
