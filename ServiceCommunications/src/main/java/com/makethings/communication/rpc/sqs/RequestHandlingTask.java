package com.makethings.communication.rpc.sqs;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.makethings.communication.rpc.ServiceManager;
import com.makethings.communication.rpc.json.JsonRpcHandler;
import com.makethings.communication.rpc.json.JsonRpcRequest;
import com.makethings.communication.rpc.json.JsonRpcResponse;

public class RequestHandlingTask implements Runnable {

    private final static Logger LOG = LoggerFactory.getLogger(RequestHandlingTask.class);

    private JsonRpcHandler jsonRpcHandler;
    private Message message;
    private SqsQueue queue;
    private String requstQueueName;
    private ServiceManager serviceManager;

    @Override
    public void run() {
        LOG.debug("Handling message: {}, from {}", message, requstQueueName);
        JsonRpcRequest jsonRpcRequest = new JsonRpcRequest().withMessages(message.getBody());
        JsonRpcResponse jsonRpcResponse = new JsonRpcResponse();
        jsonRpcResponse.withClientSessionId(jsonRpcRequest.getClientSessionId());
        jsonRpcResponse.withQueue(queue).withServiceManager(serviceManager);
        jsonRpcHandler.handle(jsonRpcRequest, jsonRpcResponse);
        deleteMessageFromQueue();
        jsonRpcResponse.send();
    }

    private void deleteMessageFromQueue() {
        queue.deleteMessage(new DeleteMessageRequest().withQueueUrl(requstQueueName).withReceiptHandle(message.getReceiptHandle()));
    }

    public void execute(Executor requestProcessingExecutor) {
        requestProcessingExecutor.execute(this);
    }

    public RequestHandlingTask withMessage(Message message) {
        this.message = message;
        return this;
    }

    public RequestHandlingTask withHandler(JsonRpcHandler jsonRpcHandler) {
        this.jsonRpcHandler = jsonRpcHandler;
        return this;
    }

    public RequestHandlingTask withQueue(SqsQueue queue) {
        this.queue = queue;
        return this;
    }

    public RequestHandlingTask withRequestQueueName(String requstQueueName) {
        this.requstQueueName = requstQueueName;
        return this;
    }

    public RequestHandlingTask withServiceManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
        return this;
    }

}
