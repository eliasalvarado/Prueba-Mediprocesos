package com.mediprocesos.backend.controller;

import com.mediprocesos.backend.dto.EmpleadoDTO;
import com.mediprocesos.backend.entity.Empleado;
import com.mediprocesos.backend.entity.Rol;
import com.mediprocesos.backend.entity.Usuario;
import com.mediprocesos.backend.service.EmpleadoService;
import com.mediprocesos.backend.service.RolService;
import com.mediprocesos.backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller para operaciones CRUD de Empleados.
 * Maneja la creación, lectura, actualización y eliminación de empleados.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/empleados")
@Tag(name = "Empleados", description = "Operaciones CRUD para empleados")
@SecurityRequirement(name = "Bearer Authentication")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Obtiene la lista de todos los empleados.
     * 
     * @return lista de todos los empleados
     */
    @GetMapping
    @Operation(summary = "Listar empleados", description = "Obtiene la lista de todos los empleados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de empleados obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmpleadoDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<List<EmpleadoDTO>> listarEmpleados() {
        log.info("GET /api/empleados");
        List<Empleado> empleados = empleadoService.obtenerTodosLosEmpleados();
        List<EmpleadoDTO> empleadosDTO = empleados.stream()
                .map(empleadoService::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(empleadosDTO);
    }

    /**
     * Obtiene un empleado por su ID.
     * 
     * @param id el ID del empleado
     * @return los datos del empleado
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener empleado por ID", description = "Obtiene los detalles de un empleado específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmpleadoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<EmpleadoDTO> obtenerEmpleado(@PathVariable Integer id) {
        log.info("GET /api/empleados/{}", id);
        Empleado empleado = empleadoService.obtenerEmpleadoPorId(id);
        return ResponseEntity.ok(empleadoService.convertirADTO(empleado));
    }

    /**
     * Crea un nuevo empleado.
     * Solo usuarios con rol ADMIN pueden crear empleados.
     * 
     * @param empleadoDTO los datos del nuevo empleado
     * @return los datos del empleado creado
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear empleado", description = "Crea un nuevo empleado (requiere rol ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empleado creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmpleadoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Empleado duplicado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<EmpleadoDTO> crearEmpleado(@Valid @RequestBody EmpleadoDTO empleadoDTO) {
        log.info("POST /api/empleados - Nombre: {}", empleadoDTO.getNombre());
        
        // Crear un nuevo usuario para el empleado
        Usuario usuario = new Usuario();
        usuario.setUsername(empleadoDTO.getUsername());
        usuario.setPassword(passwordEncoder.encode(empleadoDTO.getPassword()));
        usuario.setIsActive(true);
        
        // Asignar el rol de EMPLEADO por defecto
        Rol rolEmpleado = rolService.obtenerRolPorNombre("EMPLEADO")
                .orElseThrow(() -> new RuntimeException("Rol EMPLEADO no encontrado"));
        usuario.setRol(rolEmpleado);
        
        // Guardar el usuario
        Usuario usuarioGuardado = usuarioService.crearUsuario(usuario);
        
        // Crear el empleado con el usuario asociado
        Empleado empleado = new Empleado();
        empleado.setNombre(empleadoDTO.getNombre());
        empleado.setApellidos(empleadoDTO.getApellidos());
        empleado.setNumeroIdentificacion(empleadoDTO.getNumeroIdentificacion());
        empleado.setFechaNacimiento(empleadoDTO.getFechaNacimiento());
        empleado.setGenero(empleadoDTO.getGenero());
        empleado.setUsuario(usuarioGuardado);
        
        Empleado empleadoCreado = empleadoService.crearEmpleado(empleado);
        return ResponseEntity.status(HttpStatus.CREATED).body(empleadoService.convertirADTO(empleadoCreado));
    }

    /**
     * Actualiza los datos de un empleado.
     * Solo usuarios con rol ADMIN pueden actualizar empleados.
     * 
     * @param id el ID del empleado a actualizar
     * @param empleadoDTO los nuevos datos del empleado
     * @return los datos del empleado actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar empleado", description = "Actualiza los datos de un empleado existente (requiere rol ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmpleadoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<EmpleadoDTO> actualizarEmpleado(
            @PathVariable Integer id,
            @Valid @RequestBody EmpleadoDTO empleadoDTO) {
        log.info("PUT /api/empleados/{}", id);
        
        Empleado empleado = new Empleado();
        empleado.setNombre(empleadoDTO.getNombre());
        empleado.setApellidos(empleadoDTO.getApellidos());
        empleado.setFechaNacimiento(empleadoDTO.getFechaNacimiento());
        empleado.setGenero(empleadoDTO.getGenero());
        
        Empleado empleadoActualizado = empleadoService.actualizarEmpleado(id, empleado);
        return ResponseEntity.ok(empleadoService.convertirADTO(empleadoActualizado));
    }

    /**
     * Elimina un empleado.
     * Solo usuarios con rol ADMIN pueden eliminar empleados.
     * 
     * @param id el ID del empleado a eliminar
     * @return un mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar empleado", description = "Elimina un empleado del sistema (requiere rol ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empleado eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Integer id) {
        log.info("DELETE /api/empleados/{}", id);
        empleadoService.eliminarEmpleado(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca empleados por nombre.
     * 
     * @param nombre el nombre a buscar
     * @return lista de empleados que coinciden
     */
    @GetMapping("/buscar/nombre")
    @Operation(summary = "Buscar empleados por nombre", description = "Busca empleados cuyo nombre contenga el texto especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmpleadoDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<List<EmpleadoDTO>> buscarPorNombre(@RequestParam String nombre) {
        log.info("GET /api/empleados/buscar/nombre?nombre={}", nombre);
        List<Empleado> empleados = empleadoService.buscarEmpleadosPorNombre(nombre);
        List<EmpleadoDTO> empleadosDTO = empleados.stream()
                .map(empleadoService::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(empleadosDTO);
    }
}
