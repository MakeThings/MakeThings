package com.makethings.communication.rpc;

import com.makethings.commons.exeption.AppRuntimeExeption;

public class RemoteServiceException extends AppRuntimeExeption {

    private static final long serialVersionUID = -8727733540969921773L;

    public RemoteServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteServiceException(String message) {
        super(message);
    }

}
