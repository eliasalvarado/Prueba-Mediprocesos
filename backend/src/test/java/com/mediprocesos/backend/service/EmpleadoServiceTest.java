package com.mediprocesos.backend.service;

import com.mediprocesos.backend.entity.Empleado;
import com.mediprocesos.backend.entity.Usuario;
import com.mediprocesos.backend.entity.Rol;
import com.mediprocesos.backend.exception.EmpleadoDuplicadoException;
import com.mediprocesos.backend.exception.UsuarioNoEncontradoException;
import com.mediprocesos.backend.repository.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para EmpleadoService.
 * Prueba las operaciones CRUD de empleados.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EmpleadoService Tests")
class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoService empleadoService;

    private Empleado empleado;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        Rol rol = Rol.builder()
                .id(1)
                .nombre("EMPLEADO")
                .descripcion("Empleado")
                .build();

        usuario = Usuario.builder()
                .id(1)
                .username("juan.perez")
                .isActive(true)
                .rol(rol)
                .build();

        empleado = Empleado.builder()
                .id(1)
                .usuario(usuario)
                .nombre("Juan")
                .apellidos("Pérez García")
                .numeroIdentificacion("1234567890")
                .fechaNacimiento(LocalDate.of(1990, 5, 15))
                .genero("M")
                .build();
    }

    @Test
    @DisplayName("Obtener todos los empleados")
    void testObtenerTodosLosEmpleados() {
        // Arrange
        List<Empleado> empleados = Arrays.asList(empleado);
        when(empleadoRepository.findAll()).thenReturn(empleados);

        // Act
        List<Empleado> resultado = empleadoService.obtenerTodosLosEmpleados();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
        verify(empleadoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Obtener empleado por ID exitoso")
    void testObtenerEmpleadoPorIdExitoso() {
        // Arrange
        when(empleadoRepository.findById(1)).thenReturn(Optional.of(empleado));

        // Act
        Empleado resultado = empleadoService.obtenerEmpleadoPorId(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Juan", resultado.getNombre());
        verify(empleadoRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Obtener empleado por ID no encontrado")
    void testObtenerEmpleadoPorIdNoEncontrado() {
        // Arrange
        when(empleadoRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsuarioNoEncontradoException.class, () -> {
            empleadoService.obtenerEmpleadoPorId(999);
        });
        verify(empleadoRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("Crear empleado exitoso")
    void testCrearEmpleadoExitoso() {
        // Arrange
        when(empleadoRepository.findByNumeroIdentificacion("1234567890"))
                .thenReturn(Optional.empty());
        when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleado);

        // Act
        Empleado resultado = empleadoService.crearEmpleado(empleado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        verify(empleadoRepository, times(1)).findByNumeroIdentificacion("1234567890");
        verify(empleadoRepository, times(1)).save(any(Empleado.class));
    }

    @Test
    @DisplayName("Crear empleado duplicado")
    void testCrearEmpleadoDuplicado() {
        // Arrange
        when(empleadoRepository.findByNumeroIdentificacion("1234567890"))
                .thenReturn(Optional.of(empleado));

        // Act & Assert
        assertThrows(EmpleadoDuplicadoException.class, () -> {
            empleadoService.crearEmpleado(empleado);
        });
        verify(empleadoRepository, times(1)).findByNumeroIdentificacion("1234567890");
        verify(empleadoRepository, never()).save(any(Empleado.class));
    }

    @Test
    @DisplayName("Actualizar empleado exitoso")
    void testActualizarEmpleadoExitoso() {
        // Arrange
        Empleado empleadoActualizado = Empleado.builder()
                .nombre("Juan Luis")
                .apellidos("Pérez García")
                .build();

        when(empleadoRepository.findById(1)).thenReturn(Optional.of(empleado));
        when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleado);

        // Act
        Empleado resultado = empleadoService.actualizarEmpleado(1, empleadoActualizado);

        // Assert
        assertNotNull(resultado);
        verify(empleadoRepository, times(1)).findById(1);
        verify(empleadoRepository, times(1)).save(any(Empleado.class));
    }

    @Test
    @DisplayName("Eliminar empleado exitoso")
    void testEliminarEmpleadoExitoso() {
        // Arrange
        when(empleadoRepository.findById(1)).thenReturn(Optional.of(empleado));
        doNothing().when(empleadoRepository).delete(any(Empleado.class));

        // Act
        empleadoService.eliminarEmpleado(1);

        // Assert
        verify(empleadoRepository, times(1)).findById(1);
        verify(empleadoRepository, times(1)).delete(empleado);
    }

    @Test
    @DisplayName("Buscar empleados por nombre")
    void testBuscarEmpleadosPorNombre() {
        // Arrange
        List<Empleado> empleados = Arrays.asList(empleado);
        when(empleadoRepository.findByNombreContainingIgnoreCase("Juan"))
                .thenReturn(empleados);

        // Act
        List<Empleado> resultado = empleadoService.buscarEmpleadosPorNombre("Juan");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
        verify(empleadoRepository, times(1)).findByNombreContainingIgnoreCase("Juan");
    }

    @Test
    @DisplayName("Obtener empleado por número de identificación")
    void testObtenerEmpleadoPorNumeroIdentificacion() {
        // Arrange
        when(empleadoRepository.findByNumeroIdentificacion("1234567890"))
                .thenReturn(Optional.of(empleado));

        // Act
        Empleado resultado = empleadoService.obtenerEmpleadoPorNumeroIdentificacion("1234567890");

        // Assert
        assertNotNull(resultado);
        assertEquals("1234567890", resultado.getNumeroIdentificacion());
        verify(empleadoRepository, times(1)).findByNumeroIdentificacion("1234567890");
    }
}
