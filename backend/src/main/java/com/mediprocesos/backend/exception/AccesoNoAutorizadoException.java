package com.mediprocesos.backend.exception;

/**
 * Excepción lanzada cuando un usuario intenta acceder a un recurso sin autorización.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
public class AccesoNoAutorizadoException extends CustomException {

    /**
     * Constructor con mensaje.
     * 
     * @param message el mensaje de error
     */
    public AccesoNoAutorizadoException(String message) {
        super("ACCESO_NO_AUTORIZADO", message);
    }

    /**
     * Constructor sin parámetros.
     */
    public AccesoNoAutorizadoException() {
        super("ACCESO_NO_AUTORIZADO", "No tiene permiso para acceder a este recurso");
    }
}
