package com.makethings.communication.rpc.json;

import java.lang.reflect.Method;

import com.makethings.communication.rpc.ClientServiceException;

public interface JsonClientMarshaler {
    JsonClientRequest marshalClientRequest(String clientSessionId, Method method, Object... args) throws ClientServiceException;
    Object demarshalClientResponse(JsonClientResponse response) throws ClientServiceException;
}
