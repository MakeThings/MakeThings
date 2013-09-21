package com.makethings.communication.rpc.json;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.googlecode.jsonrpc4j.JsonRpcClient;
import com.googlecode.jsonrpc4j.ReflectionUtil;
import com.makethings.communication.rpc.ClientServiceException;

public class DefultJsonClientMarshaler implements JsonClientMarshaler {

    private final static Logger LOG = LoggerFactory.getLogger(DefultJsonClientMarshaler.class);

    private JsonRpcClient jsonRpcClient;

    public DefultJsonClientMarshaler() {
        jsonRpcClient = new JsonRpcClient();
    }

    @Override
    public Object demarshalClientResponse(JsonClientResponse response) {
        Object result = null;

        LOG.info("Demarshalling json rpc response: {}", response.getJsonRpcResponse());

        try {
            InputStream jsonResponseByteArray = new ByteArrayInputStream(response.getJsonRpcResponseAsByteArray());
            result = jsonRpcClient.readResponse(response.getServiceMethodResultType(), jsonResponseByteArray);
            
            LOG.info("Demarshalled result: {} by reponse: {}", result, response.getJsonRpcResponse());
        }
        catch (Throwable e) {
            throw new ClientServiceException("Cannot demarshal response", e);
        }
        return result;
    }

    @Override
    public JsonClientRequest marshalClientRequest(String clientSessionId, Method method, Object... args) throws ClientServiceException {
        LOG.info("Marshaling request for method: {} with args: {}", method.getName(), args);
        
        
        ObjectNode requestNode = JsonNodeFactory.instance.objectNode();

        populateSessionId(requestNode, clientSessionId);

        UUID requestId = UUID.randomUUID();
        populateRpcRequest(requestId, requestNode, method, args);

        return new JsonClientRequest(method, requestNode.toString(), requestId.toString());
    }

    private void populateSessionId(ObjectNode requestNode, String clientSessionId) {
        requestNode.put("SId", clientSessionId);
    }

    private void populateRpcRequest(UUID requestId, ObjectNode requestNode, Method method, Object... args) throws ClientServiceException {
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
