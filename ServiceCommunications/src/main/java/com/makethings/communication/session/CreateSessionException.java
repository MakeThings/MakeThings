package com.makethings.communication.session;

import com.makethings.commons.exeption.AppRuntimeExeption;

public class CreateSessionException extends AppRuntimeExeption {

    private static final long serialVersionUID = -7209434044073156187L;

    public CreateSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateSessionException(String message) {
        super(message);
    }

}
