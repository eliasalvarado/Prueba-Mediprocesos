package com.mediprocesos.backend.service;

import com.mediprocesos.backend.entity.Rol;
import com.mediprocesos.backend.repository.RolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para operaciones CRUD de Rol.
 * Maneja la lógica de negocio relacionada con roles.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
@Slf4j
@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    /**
     * Obtiene todos los roles disponibles.
     * 
     * @return lista de todos los roles
     */
    public List<Rol> obtenerTodosLosRoles() {
        log.info("Obteniendo todos los roles");
        return rolRepository.findAll();
    }

    /**
     * Obtiene un rol por su nombre.
     * 
     * @param nombre el nombre del rol
     * @return Optional con el rol si existe
     */
    public Optional<Rol> obtenerRolPorNombre(String nombre) {
        log.info("Obteniendo rol por nombre: {}", nombre);
        return rolRepository.findByNombre(nombre);
    }

    /**
     * Obtiene un rol por su ID.
     * 
     * @param id el ID del rol
     * @return Optional con el rol si existe
     */
    public Optional<Rol> obtenerRolPorId(Integer id) {
        log.info("Obteniendo rol por ID: {}", id);
        return rolRepository.findById(id);
    }

    /**
     * Crea un nuevo rol.
     * 
     * @param rol el rol a crear
     * @return el rol creado
     */
    public Rol crearRol(Rol rol) {
        log.info("Creando nuevo rol: {}", rol.getNombre());
        return rolRepository.save(rol);
    }
}
