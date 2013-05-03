package com.makethings.communication.rpc.json;

import java.io.OutputStream;

public class JsonRpcResponse {

    private OutputStream os;

    public String getMessage() {
        return "Json response...";
    }

    public OutputStream getOutputStream() {
        return os;
    }

    public JsonRpcResponse withOutputStream(OutputStream os) {
        this.os = os;
        return this;
    }

}
