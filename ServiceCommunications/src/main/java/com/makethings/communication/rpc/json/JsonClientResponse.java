package com.makethings.communication.rpc.json;

import java.lang.reflect.Method;

public class JsonClientResponse {
    private String jsonRpcResponse;
    private Method invokedMethod;
    private String reponseId;

    public JsonClientResponse(ClientResponseMessageWrapper messageWrapper, Method method) {
        invokedMethod = method;
        reponseId = messageWrapper.getResponseId();
        jsonRpcResponse = messageWrapper.getJsonRpc();
    }

    public String getJsonRpcResponse() {
        return jsonRpcResponse;
    }
    
    public byte[] getJsonRpcResponseAsByteArray() {
        return getJsonRpcResponse().getBytes();
    }
    

    public Method getInvokedMethod() {
        return invokedMethod;
    }
    
    public Class<?> getServiceMethodResultType() {
        return getInvokedMethod().getReturnType();
    }

    public String getReponseId() {
        return reponseId;
    }
}
