package com.makethings.communication.rpc.sqs;

import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.makethings.communication.rpc.json.JsonClientRequest;

public class SqsRpcRequestSender {
    private final String requestQueueName;
    private final SqsQueue queue;
    
    public SqsRpcRequestSender(String requestQueueName, SqsQueue queue) {
        this.requestQueueName = requestQueueName;
        this.queue = queue;
    }

    public void send(JsonClientRequest request) {
        queue.sendMessage(new SendMessageRequest(requestQueueName, request.getMessage()));
    }
}
