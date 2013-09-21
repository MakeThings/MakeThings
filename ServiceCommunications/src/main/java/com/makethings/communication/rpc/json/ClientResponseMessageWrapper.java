package com.makethings.communication.rpc.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientResponseMessageWrapper {
    private final String serviceResponseMessage;
    private JsonNode json;

    public ClientResponseMessageWrapper(String serviceResponseMessage) throws JsonClientServiceException {
        this.serviceResponseMessage = serviceResponseMessage;
        parse();
    }

    private void parse() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.readTree(serviceResponseMessage);
        }
        catch (JsonProcessingException e) {
            throw new JsonClientServiceException("Incoming message: " + serviceResponseMessage + " cannot be parsed.", e);
        }
        catch (IOException e) {
            throw new JsonClientServiceException("Incoming message: " + serviceResponseMessage + " cannot be parsed.", e);
        }
    }

    public boolean isResponseOnRequest(String requestId) {
        return getResponseId().equals(requestId);
    }
    
    public String getResponseId() {
        return json.get("Res").get("id").asText();
    }

    public String getJsonRpc() {
        return json.get("Res").toString();
    }
}
