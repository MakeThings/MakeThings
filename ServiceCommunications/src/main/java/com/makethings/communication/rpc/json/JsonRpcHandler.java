package com.makethings.communication.rpc.json;

import com.fasterxml.jackson.databind.JsonNode;

public interface JsonRpcHandler {
    void handle(JsonRpcRequest request, JsonRpcResponse response);
}
