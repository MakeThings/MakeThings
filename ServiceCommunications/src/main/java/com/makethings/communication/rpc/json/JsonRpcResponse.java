package com.makethings.communication.rpc.json;

import java.io.OutputStream;

import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.makethings.communication.rpc.ServiceManager;
import com.makethings.communication.rpc.sqs.SqsQueue;

public class JsonRpcResponse {

    private OutputStream os;
    private String clientSessionId;
    private SqsQueue queue;
    private ServiceManager serviceManager;

    public OutputStream getOutputStream() {
        return os;
    }

    public JsonRpcResponse withOutputStream(OutputStream os) {
        this.os = os;
        return this;
    }

    public JsonRpcResponse withClientSessionId(String clientSessionId) {
        this.clientSessionId = clientSessionId;
        return this;
    }

    public JsonRpcResponse withQueue(SqsQueue queue) {
        this.queue = queue;
        return this;
    }

    public JsonRpcResponse withServiceManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
        return this;
    }

    public void send() {
        String responseQueueName = serviceManager.getClientResponseQueueName(clientSessionId);
        SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(responseQueueName).withMessageBody(
                formatServiceResponse());
        queue.sendMessage(sendMessageRequest);
    }

    private String formatServiceResponse() {
        return "Json response...";
    }

}
