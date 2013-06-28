package com.makethings.communication.rpc.json;

import java.lang.reflect.Method;

public class JsonClientRequest {

    private final String message;
    private final String requestId;
    
    // TODO: add population
    private Method method;

    public JsonClientRequest(String message, String requestId) {
        this.message = message;
        this.requestId = requestId;
    }

    public String getMessage() {
        return message;
    }

    public String getRequestId() {
        return requestId;
    }

}
