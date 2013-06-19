package com.makethings.communication.rpc.json;


public class JsonClientRequest {

    private final String message;
    private final String requestId;

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
