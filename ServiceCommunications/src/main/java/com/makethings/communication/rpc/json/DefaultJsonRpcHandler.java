package com.makethings.communication.rpc.json;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcServer;
import com.makethings.communication.rpc.RemoteServiceException;
import com.makethings.communication.rpc.sqs.RequestHandlingTask;

public class DefaultJsonRpcHandler implements JsonRpcHandler {

    private final static Logger LOG = LoggerFactory.getLogger(RequestHandlingTask.class);

    private JsonRpcServer jsonRpcServer;
    private Object service;
    private Class<?> serviceInterfaceClass;

    @Override
    public void handle(JsonRpcRequest request, JsonRpcResponse response) {
        LOG.debug("Handling json request {}", request);
        
        JsonNode jsonNode = request.asJsonNode();
        try {
            getJsonRpcService().handleNode(jsonNode, response.getOutputStream());
        }
        catch (IOException e) {
            throw new RemoteServiceException("Error handling service request: " + request.toString(), e);
        }
    }

    
    private JsonRpcServer getJsonRpcService() {
        if (jsonRpcServer == null) {
            LOG.info("Creating instance of JsonRpcServer for serviceInterfaceClass: {}", serviceInterfaceClass.getName());
            jsonRpcServer = new JsonRpcServer(new ObjectMapper(), service, serviceInterfaceClass);
            jsonRpcServer.setErrorResolver(JsonRpcServer.DEFAULT_ERRROR_RESOLVER);
        }
        return jsonRpcServer;
    }

    public Object getService() {
        return service;
    }

    public void setService(Object service) {
        this.service = service;
    }

    public Class<?> getServiceInterfaceClass() {
        return serviceInterfaceClass;
    }

    public void setServiceInterfaceClass(Class<?> serviceInterfaceClass) {
        this.serviceInterfaceClass = serviceInterfaceClass;
    }
}
