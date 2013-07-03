package com.makethings.communication.rpc.sqs;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.makethings.communication.rpc.json.ClientResponseMessageWrapper;
import com.makethings.communication.rpc.json.JsonClientRequest;
import com.makethings.communication.rpc.json.JsonClientResponse;

public class SqsRpcResponseReceiver {
    private final SqsQueue queue;
    private final String responseQueueName;
    private int waitResponseTimeout = 10;

    public SqsRpcResponseReceiver(String responseQueueName, SqsQueue queue) {
        this.responseQueueName = responseQueueName;
        this.queue = queue;
    }

    public JsonClientResponse receiveResponseFor(JsonClientRequest jsonClientRequest) {
        JsonClientResponse result = null;
        ReceiveMessageRequest receiveMsgRequest = new ReceiveMessageRequest(responseQueueName);
        ReceiveMessageResult receiveMsgResult = queue.receiveMessage(receiveMsgRequest);
        for (Message m : receiveMsgResult.getMessages()) {
            ClientResponseMessageWrapper messageWrapper = new ClientResponseMessageWrapper(m.getBody());
            if (messageWrapper.responseOnRequest(jsonClientRequest.getRequestId())) {
                result = new JsonClientResponse(messageWrapper);
            }
        }

        return result;
    }
}
