package com.makethings.communication.rpc.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

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
    public JsonClientRequest marshalClientRequest(Method method, Object... args) {
        ObjectNode requestNode = JsonNodeFactory.instance.objectNode();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Object arguments = ReflectionUtil.parseArguments(method, args,
                true);
        try {
            jsonRpcClient.writeRequest(method.getName(), arguments, outputStream, "10");
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //requestNode.put("SId", session.getId());
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
        JsonClientRequest result = new JsonClientRequest(requestNode.toString());
        return result;
    }

}
