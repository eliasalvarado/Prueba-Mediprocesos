package com.mediprocesos.backend.exception;

/**
 * Excepción lanzada cuando se intenta crear un empleado con un identificador duplicado.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
public class EmpleadoDuplicadoException extends CustomException {

    /**
     * Constructor con mensaje.
     * 
     * @param message el mensaje de error
     */
    public EmpleadoDuplicadoException(String message) {
        super("EMPLEADO_DUPLICADO", message);
    }

    /**
     * Constructor sin parámetros.
     */
    public EmpleadoDuplicadoException() {
        super("EMPLEADO_DUPLICADO", "Ya existe un empleado con este identificador");
    }
}
