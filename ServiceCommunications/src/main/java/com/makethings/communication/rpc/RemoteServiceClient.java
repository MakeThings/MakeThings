package com.makethings.communication.rpc;

import java.lang.reflect.Method;

public interface RemoteServiceClient {
    void init();
    Object invoke(Method declaredMethod, Object... args);
}
