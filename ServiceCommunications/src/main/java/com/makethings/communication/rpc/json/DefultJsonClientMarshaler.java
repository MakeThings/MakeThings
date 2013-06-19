package com.makethings.communication.rpc.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.googlecode.jsonrpc4j.JsonRpcClient;
import com.googlecode.jsonrpc4j.ReflectionUtil;

public class DefultJsonClientMarshaler implements JsonClientMarshaler {

    private JsonRpcClient jsonRpcClient;

    public DefultJsonClientMarshaler() {
        jsonRpcClient = new JsonRpcClient();
    }

    @Override
    public JsonClientRequest marshalClientRequest(String clientSessionId, Method method, Object... args) {
        ObjectNode requestNode = JsonNodeFactory.instance.objectNode();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Object arguments = ReflectionUtil.parseArguments(method, args, true);
        UUID requestId = UUID.randomUUID();
        try {
            jsonRpcClient.writeRequest(method.getName(), arguments, outputStream, requestId.toString());
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        requestNode.put("SId", clientSessionId);
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestNode.put("Req", mapper.readTree(outputStream.toByteArray()));
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        JsonClientRequest result = new JsonClientRequest(requestNode.toString(), requestId.toString());
        return result;
    }

}
