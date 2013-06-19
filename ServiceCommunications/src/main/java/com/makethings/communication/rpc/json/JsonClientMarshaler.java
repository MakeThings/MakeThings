package com.makethings.communication.rpc.json;

import java.lang.reflect.Method;

public interface JsonClientMarshaler {
    public JsonClientRequest marshalClientRequest(String clientSessionId, Method method, Object... args);
}
