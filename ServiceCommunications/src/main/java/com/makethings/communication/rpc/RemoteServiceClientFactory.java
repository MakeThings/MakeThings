package com.makethings.communication.rpc;

public interface RemoteServiceClientFactory<T> {
    T create();
}
