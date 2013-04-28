package com.makethings.communication.rpc.json;

public interface JsonRpcHandler {
    JsonRpcResponse handle(JsonRpcRequest r);
}
