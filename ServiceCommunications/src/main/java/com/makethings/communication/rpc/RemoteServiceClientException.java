package com.makethings.communication.rpc;

import com.makethings.commons.exeption.AppRuntimeExeption;

public class RemoteServiceClientException extends AppRuntimeExeption {

    private static final long serialVersionUID = 5977674560789824146L;

    public RemoteServiceClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteServiceClientException(String message) {
        super(message);
    }

}
