package com.makethings.communication.rpc.json;

import java.lang.reflect.Method;

public class JsonClientResponse {
    String receivedMessage;
    Method invokedMethod;
    String reponseId;

    public JsonClientResponse(ClientResponseMessageWrapper messageWrapper) {
        reponseId = messageWrapper.getResponseId();
    }

    public String getReceivedMessage() {
        return receivedMessage;
    }

    public Method getInvokedMethod() {
        return invokedMethod;
    }

    public String getReponseId() {
        return reponseId;
    }

}
