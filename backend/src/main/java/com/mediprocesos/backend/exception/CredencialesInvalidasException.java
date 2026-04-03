package com.mediprocesos.backend.exception;

/**
 * Excepción lanzada cuando las credenciales proporcionadas son inválidas.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
public class CredencialesInvalidasException extends CustomException {

    /**
     * Constructor con mensaje.
     * 
     * @param message el mensaje de error
     */
    public CredencialesInvalidasException(String message) {
        super("CREDENCIALES_INVALIDAS", message);
    }

    /**
     * Constructor sin parámetros.
     */
    public CredencialesInvalidasException() {
        super("CREDENCIALES_INVALIDAS", "Las credenciales proporcionadas son inválidas");
    }
}
