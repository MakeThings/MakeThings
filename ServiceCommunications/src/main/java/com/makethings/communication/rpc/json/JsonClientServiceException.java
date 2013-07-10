package com.makethings.communication.rpc.json;

import com.makethings.communication.rpc.ClientServiceException;

public class JsonClientServiceException extends ClientServiceException {

    private static final long serialVersionUID = 8571225809263895783L;

    public JsonClientServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonClientServiceException(String message) {
        super(message);
    }

}
