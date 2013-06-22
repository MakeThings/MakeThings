package com.makethings.communication.rpc;

import com.makethings.commons.exeption.AppRuntimeExeption;

public class ClientServiceException extends AppRuntimeExeption {

    private static final long serialVersionUID = -5335444886255199676L;

    public ClientServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientServiceException(String message) {
        super(message);
    }
    
}
