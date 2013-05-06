package com.makethings.communication.rpc.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.makethings.communication.rpc.RemoteServiceException;
import com.makethings.communication.rpc.ServiceManager;
import com.makethings.communication.rpc.sqs.SqsQueue;

public class JsonServiceResponse {

    private final static Logger LOG = LoggerFactory.getLogger(JsonServiceResponse.class);

    private ByteArrayOutputStream rpcOs;
    private String clientSessionId;
    private SqsQueue queue;
    private ServiceManager serviceManager;

    public JsonServiceResponse() {
        this.rpcOs = new ByteArrayOutputStream();
    }

    public OutputStream getOutputStream() {
        return rpcOs;
    }

    public JsonServiceResponse withClientSessionId(String clientSessionId) {
        this.clientSessionId = clientSessionId;
        return this;
    }

    public JsonServiceResponse withQueue(SqsQueue queue) {
        this.queue = queue;
        return this;
    }

    public JsonServiceResponse withServiceManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
        return this;
    }

    public void send() {
        String responseQueueName = serviceManager.getClientResponseQueueName(clientSessionId);

        SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(responseQueueName);
        String jsonServiceResponseMessage = getMessage();
        LOG.debug("Sending Json Service Response: {}, to: {}", jsonServiceResponseMessage, responseQueueName);
        sendMessageRequest.withMessageBody(jsonServiceResponseMessage);

        queue.sendMessage(sendMessageRequest);
    }

    public String getMessage() {
        ObjectMapper mapper = new ObjectMapper();
        String rpcMessage = rpcOs.toString();
        try {
            JsonNode rpcJsonNode = mapper.readTree(rpcMessage);
            ObjectNode responseNode = JsonNodeFactory.instance.objectNode().objectNode();
            responseNode.put("Res", rpcJsonNode);
            return responseNode.toString();
        }
        catch (JsonProcessingException e) {
            throw new RemoteServiceException("Cannot marshall json service reponse, message: " + rpcMessage + " ,error: " + e.getMessage(), e);
        }
        catch (IOException e) {
            throw new RemoteServiceException("Cannot marshall json service reponse, error: " + e.getMessage(), e);
        }
    }

}
