package com.mediprocesos.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediprocesos.backend.dto.EmpleadoDTO;
import com.mediprocesos.backend.entity.Empleado;
import com.mediprocesos.backend.service.EmpleadoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración para EmpleadoController.
 * Prueba los endpoints REST del CRUD de empleados.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("EmpleadoController Tests")
class EmpleadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmpleadoService empleadoService;

    private Empleado empleado;
    private EmpleadoDTO empleadoDTO;

    @BeforeEach
    void setUp() {
        empleado = Empleado.builder()
                .id(1)
                .nombre("Juan")
                .apellidos("Pérez García")
                .numeroIdentificacion("1234567890")
                .fechaNacimiento(LocalDate.of(1990, 5, 15))
                .genero("M")
                .build();

        empleadoDTO = EmpleadoDTO.builder()
                .id(1)
                .nombre("Juan")
                .apellidos("Pérez García")
                .numeroIdentificacion("1234567890")
                .fechaNacimiento(LocalDate.of(1990, 5, 15))
                .genero("M")
                .build();
    }

    @Test
    @DisplayName("GET /api/empleados - Listar todos los empleados")
    void testListarEmpleados() throws Exception {
        // Arrange
        List<Empleado> empleados = Arrays.asList(empleado);
        when(empleadoService.obtenerTodosLosEmpleados()).thenReturn(empleados);
        when(empleadoService.convertirADTO(empleado)).thenReturn(empleadoDTO);

        // Act & Assert
        mockMvc.perform(get("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Juan"));

        verify(empleadoService, times(1)).obtenerTodosLosEmpleados();
    }

    @Test
    @DisplayName("GET /api/empleados/{id} - Obtener empleado por ID")
    void testObtenerEmpleadoPorId() throws Exception {
        // Arrange
        when(empleadoService.obtenerEmpleadoPorId(1)).thenReturn(empleado);
        when(empleadoService.convertirADTO(empleado)).thenReturn(empleadoDTO);

        // Act & Assert
        mockMvc.perform(get("/api/empleados/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"));

        verify(empleadoService, times(1)).obtenerEmpleadoPorId(1);
    }

    @Test
    @DisplayName("POST /api/empleados - Crear nuevo empleado (requiere ADMIN)")
    void testCrearEmpleado() throws Exception {
        // Arrange
        when(empleadoService.crearEmpleado(any(Empleado.class))).thenReturn(empleado);
        when(empleadoService.convertirADTO(empleado)).thenReturn(empleadoDTO);

        // Act & Assert
        mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"));

        verify(empleadoService, times(1)).crearEmpleado(any(Empleado.class));
    }

    @Test
    @DisplayName("PUT /api/empleados/{id} - Actualizar empleado (requiere ADMIN)")
    void testActualizarEmpleado() throws Exception {
        // Arrange
        when(empleadoService.actualizarEmpleado(anyInt(), any(Empleado.class))).thenReturn(empleado);
        when(empleadoService.convertirADTO(empleado)).thenReturn(empleadoDTO);

        // Act & Assert
        mockMvc.perform(put("/api/empleados/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"));

        verify(empleadoService, times(1)).actualizarEmpleado(anyInt(), any(Empleado.class));
    }

    @Test
    @DisplayName("DELETE /api/empleados/{id} - Eliminar empleado (requiere ADMIN)")
    void testEliminarEmpleado() throws Exception {
        // Arrange
        doNothing().when(empleadoService).eliminarEmpleado(1);

        // Act & Assert
        mockMvc.perform(delete("/api/empleados/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(empleadoService, times(1)).eliminarEmpleado(1);
    }

    @Test
    @DisplayName("GET /api/empleados/buscar/nombre - Buscar por nombre")
    void testBuscarEmpleadosPorNombre() throws Exception {
        // Arrange
        List<Empleado> empleados = Arrays.asList(empleado);
        when(empleadoService.buscarEmpleadosPorNombre("Juan")).thenReturn(empleados);
        when(empleadoService.convertirADTO(empleado)).thenReturn(empleadoDTO);

        // Act & Assert
        mockMvc.perform(get("/api/empleados/buscar/nombre?nombre=Juan")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"));

        verify(empleadoService, times(1)).buscarEmpleadosPorNombre("Juan");
    }
}
