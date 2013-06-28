package com.makethings.communication.rpc.json;

import java.lang.reflect.Method;

public interface JsonClientMarshaler {
    JsonClientRequest marshalClientRequest(String clientSessionId, Method method, Object... args);
    Object demarshalClientResponse(JsonClientResponse response);
}
