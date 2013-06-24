package com.makethings.communication.rpc.sqs;

import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.makethings.communication.rpc.json.JsonClientRequest;
import com.makethings.communication.rpc.json.JsonClientResponse;

public class SqsRpcResponseReceiver {
    private final SqsQueue queue;
    private final String responseQueueName;

    public SqsRpcResponseReceiver(String responseQueueName, SqsQueue queue) {
        this.responseQueueName = responseQueueName;
        this.queue = queue;
    }

    public JsonClientResponse receiveResponseFor(JsonClientRequest jsonClientRequest) {
        
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(responseQueueName);
        ReceiveMessageResult receiveMessageResult = queue.receiveMessage(receiveMessageRequest);
        return null;
    }
}
