package com.makethings.communication.rpc.json;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.googlecode.jsonrpc4j.JsonRpcClient;
import com.googlecode.jsonrpc4j.ReflectionUtil;
import com.makethings.communication.rpc.ClientServiceException;

public class DefultJsonClientMarshaler implements JsonClientMarshaler {

    private JsonRpcClient jsonRpcClient;

    public DefultJsonClientMarshaler() {
        jsonRpcClient = new JsonRpcClient();
    }

    @Override
    public Object demarshalClientResponse(JsonClientResponse response) {
        return null;
    }

    
    @Override
    public JsonClientRequest marshalClientRequest(String clientSessionId, Method method, Object... args) {
        ObjectNode requestNode = JsonNodeFactory.instance.objectNode();
        
        populateSessionId(requestNode, clientSessionId);
        
        UUID requestId = UUID.randomUUID();
        populateRpcRequest(requestId, requestNode, method, args);
        
        return new JsonClientRequest(requestNode.toString(), requestId.toString());
    }

    private void populateSessionId(ObjectNode requestNode, String clientSessionId) {
        requestNode.put("SId", clientSessionId);
    }

    private void populateRpcRequest(UUID requestId, ObjectNode requestNode, Method method, Object... args) {
        try {
            Object arguments = ReflectionUtil.parseArguments(method, args, true);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            jsonRpcClient.writeRequest(method.getName(), arguments, outputStream, requestId.toString());
            JsonNode jsonRpcRequest = new ObjectMapper().readTree(outputStream.toByteArray());
            
            requestNode.put("Req", jsonRpcRequest);
        }
        catch (Exception e) {
            throw new ClientServiceException("Cannot write json rpc request for method: " + method + " and args: " + args);
        }
    }

  
}
