package com.makethings.communication.rpc.json;

import com.googlecode.jsonrpc4j.JsonRpcParam;

public interface TestIdeaService {
    int createNewIdea(@JsonRpcParam("name") String name);
}
