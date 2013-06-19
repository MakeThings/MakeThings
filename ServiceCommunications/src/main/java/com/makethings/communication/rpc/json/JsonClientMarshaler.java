package com.makethings.communication.rpc.json;

import java.lang.reflect.Method;

public interface JsonClientMarshaler {
    public JsonClientRequest marshalClientRequest(Method method, Object... args);
}
