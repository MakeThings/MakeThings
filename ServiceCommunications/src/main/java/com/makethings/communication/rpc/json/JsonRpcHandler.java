package com.makethings.communication.rpc.json;

import com.makethings.communication.rpc.RemoteServiceException;

public interface JsonRpcHandler {
    void handle(JsonServiceRequest request, JsonServiceResponse response) throws RemoteServiceException;
}
