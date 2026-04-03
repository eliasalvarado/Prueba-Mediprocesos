package com.mediprocesos.backend.exception;

/**
 * Excepción lanzada cuando no se encuentra un usuario.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
public class UsuarioNoEncontradoException extends CustomException {

    /**
     * Constructor con mensaje.
     * 
     * @param message el mensaje de error
     */
    public UsuarioNoEncontradoException(String message) {
        super("USUARIO_NO_ENCONTRADO", message);
    }

    /**
     * Constructor sin parámetros.
     */
    public UsuarioNoEncontradoException() {
        super("USUARIO_NO_ENCONTRADO", "Usuario no encontrado");
    }
}
