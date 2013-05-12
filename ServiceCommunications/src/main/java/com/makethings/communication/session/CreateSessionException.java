package com.makethings.communication.session;


public class CreateSessionException extends SessionException {

    private static final long serialVersionUID = -7209434044073156187L;

    public CreateSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateSessionException(String message) {
        super(message);
    }

}
