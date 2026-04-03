package com.mediprocesos.backend.exception;

/**
 * Excepción personalizada base para la aplicación.
 * Todas las excepciones personalizadas heredarán de esta clase.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
public class CustomException extends RuntimeException {

    /**
     * Código de error asociado a la excepción.
     */
    private String code;

    /**
     * Constructor con mensaje.
     * 
     * @param message el mensaje de error
     */
    public CustomException(String message) {
        super(message);
        this.code = "GENERIC_ERROR";
    }

    /**
     * Constructor con mensaje y código.
     * 
     * @param code el código de error
     * @param message el mensaje de error
     */
    public CustomException(String code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * Constructor con mensaje y causa.
     * 
     * @param message el mensaje de error
     * @param cause la causa del error
     */
    public CustomException(String message, Throwable cause) {
        super(message, cause);
        this.code = "GENERIC_ERROR";
    }

    /**
     * Obtiene el código de error.
     * 
     * @return el código de error
     */
    public String getCode() {
        return code;
    }
}
