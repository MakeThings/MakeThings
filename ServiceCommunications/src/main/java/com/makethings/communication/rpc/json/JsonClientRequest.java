package com.makethings.communication.rpc.json;

import java.lang.reflect.Method;

public class JsonClientRequest {

    private final String message;
    private final String requestId;
    private final Method method;

    public JsonClientRequest(Method method, String message, String requestId) {
        this.method = method;
        this.message = message;
        this.requestId = requestId;
    }

    public String getMessage() {
        return message;
    }

    public String getRequestId() {
        return requestId;
    }

    public Method getMethod() {
        return method;
    }
}
