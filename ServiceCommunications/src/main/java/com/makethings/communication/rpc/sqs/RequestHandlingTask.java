package com.makethings.communication.rpc.sqs;

import java.util.concurrent.Executor;

import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.makethings.communication.rpc.json.JsonRpcHandler;
import com.makethings.communication.rpc.json.JsonRpcRequest;

public class RequestHandlingTask implements Runnable {

    private JsonRpcHandler jsonRpcHandler;
    private Message message;
    private SqsQueue queue;
    private String requstQueueName;

    @Override
    public void run() {
        jsonRpcHandler.handle(new JsonRpcRequest().withMessages(message.getBody()));
        deleteMessageFromQueue();
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

}
