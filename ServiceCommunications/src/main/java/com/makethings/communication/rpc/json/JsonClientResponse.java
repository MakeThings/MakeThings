package com.makethings.communication.rpc.json;

import java.lang.reflect.Method;

public class JsonClientResponse {
    String jsonRpcResponse;
    Method invokedMethod;
    String reponseId;

    public JsonClientResponse(ClientResponseMessageWrapper messageWrapper) {
        reponseId = messageWrapper.getResponseId();
        jsonRpcResponse = messageWrapper.getJsonRpc();
    }

    public String getJsonRpcResponse() {
        return jsonRpcResponse;
    }

    public Method getInvokedMethod() {
        return invokedMethod;
    }

    public String getReponseId() {
        return reponseId;
    }

}
