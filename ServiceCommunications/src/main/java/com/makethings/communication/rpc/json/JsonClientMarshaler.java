package com.makethings.communication.rpc.json;

import java.lang.reflect.Method;

public interface JsonClientMarshaler {
    public String marshalClientRequest(Method method, Object... args);
}
