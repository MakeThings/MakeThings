package com.makethings.communication.rpc.json;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.makethings.communication.rpc.RemoteServiceException;

public class JsonServiceRequest {

    private final static Logger LOG = LoggerFactory.getLogger(JsonServiceRequest.class);

    private String message;
    private JsonNode serviceRequest;

    public JsonServiceRequest withMessages(String message) {
        this.message = message;
        return this;
    }

    public JsonServiceRequest parse() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            serviceRequest = mapper.readTree(message);
            LOG.debug("Parsed json node {}", serviceRequest);
        }
        catch (JsonProcessingException e) {
            throw new RemoteServiceException("Error parsing json request: " + message + ", error: " + e.getMessage(), e);
        }
        catch (IOException e) {
            throw new RemoteServiceException("Error parsing json request: " + message + ", error: " + e.getMessage(), e);
        }
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JsonServiceRequest other = (JsonServiceRequest) obj;
        if (other.message == null ^ this.message == null)
            return false;
        if (other.message != null && other.message.equals(this.message) == false)
            return false;

        return true;
    }

    public String getClientSessionId() {
        if (!isParsed()) {
            parse();
        }
        return extractClientSessionId();
    }

    private String extractClientSessionId() {
        JsonNode jsonNode = serviceRequest.get("SId");
        String clientSessionId = jsonNode.asText();
        LOG.debug("Extracted Client Session Id {}", clientSessionId);
        return clientSessionId;
    }

    @Override
    public String toString() {
        return "JsonRpcRequest [message=" + message + "]";
    }

    public JsonNode getRpcRequest() {
        if (!isParsed()) {
            parse();
        }
        return extractRpcRequest();
    }

    private JsonNode extractRpcRequest() {
        JsonNode rpcRequest = serviceRequest.get("Req");
        LOG.debug("Extracted RPC request {}", rpcRequest);
        return rpcRequest;
    }

    private boolean isParsed() {
        return serviceRequest != null;
    }
}
