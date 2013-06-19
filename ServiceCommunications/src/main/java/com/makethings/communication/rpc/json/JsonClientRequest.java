package com.makethings.communication.rpc.json;

public class JsonClientRequest {

    private final String message;

    public JsonClientRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    
}
