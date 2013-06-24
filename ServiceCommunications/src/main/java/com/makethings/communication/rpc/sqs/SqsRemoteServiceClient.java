package com.makethings.communication.rpc.sqs;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.makethings.communication.rpc.AbstractRemoteServiceClient;
import com.makethings.communication.rpc.json.JsonClientRequest;
import com.makethings.communication.rpc.json.JsonClientResponse;

public class SqsRemoteServiceClient extends AbstractRemoteServiceClient {

    private final static Logger LOG = LoggerFactory.getLogger(SqsRemoteServiceClient.class);

    private SqsQueue queue;
    private SqsRpcRequestSender sqsRpcRequestSender;
    private SqsRpcResponseReceiver sqsRpcResponseReceiver;

    @Override
    protected void onInit() {
        LOG.debug("Creating SQS queue with name: {}", session.getResponseQueueName());
        CreateQueueResult createQueueResult = queue.createQueue(new CreateQueueRequest(session.getResponseQueueName()));
        LOG.info("Created SQS queue with details: {}", createQueueResult);
        sqsRpcRequestSender = new SqsRpcRequestSender(requestQueueName, queue);
        sqsRpcResponseReceiver = new SqsRpcResponseReceiver(session.getResponseQueueName(), queue);
    }

    public Object invoke(Method declaredMethod, Object... args) {
        LOG.debug("Sending RPC request for method: {} and args: {}", declaredMethod.getName(), args);
        
        JsonClientRequest request = jsonClientMarshaler.marshalClientRequest(session.getId(), declaredMethod, args);
        sqsRpcRequestSender.send(request);
        JsonClientResponse response = sqsRpcResponseReceiver.receiveResponseFor(request);
        
        return null;
    }

    public void setQueue(SqsQueue queue) {
        this.queue = queue;
    }

    public SqsRpcRequestSender getSqsRpcRequestSender() {
        return sqsRpcRequestSender;
    }

    public void setSqsRpcRequestSender(SqsRpcRequestSender sqsRpcRequestSender) {
        this.sqsRpcRequestSender = sqsRpcRequestSender;
    }

    public SqsRpcResponseReceiver getSqsRpcResponseReceiver() {
        return sqsRpcResponseReceiver;
    }

    public void setSqsRpcResponseReceiver(SqsRpcResponseReceiver sqsRpcResponseReceiver) {
        this.sqsRpcResponseReceiver = sqsRpcResponseReceiver;
    }

}
