package com.makethings.communication.rpc.sqs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.makethings.communication.rpc.RemoteServiceClientException;
import com.makethings.communication.rpc.json.ClientResponseMessageWrapper;
import com.makethings.communication.rpc.json.JsonClientRequest;
import com.makethings.communication.rpc.json.JsonClientResponse;

public class SqsRpcResponseReceiver {

	private final static Logger LOG = LoggerFactory.getLogger(SqsRpcResponseReceiver.class);
	
    public static final long DEFAULT_RECEIVE_TIMEOUT = 10000;

    private final SqsQueue queue;
    private final String responseQueueName;
    private long receiveTimeout = DEFAULT_RECEIVE_TIMEOUT;

    public SqsRpcResponseReceiver(String responseQueueName, SqsQueue queue) {
        this.responseQueueName = responseQueueName;
        this.queue = queue;
    }

    public JsonClientResponse receiveResponseFor(JsonClientRequest jsonClientRequest) throws RemoteServiceClientException {
    	LOG.debug("Receiving reponse for {}", jsonClientRequest);
    	
    	JsonClientResponse result = null;
        ReceiveMessageRequest receiveMsgRequest = new ReceiveMessageRequest(responseQueueName);
        long started = System.currentTimeMillis();
        while (result == null && !untilTimeout(started)) {
            ReceiveMessageResult receiveMsgResult = queue.receiveMessage(receiveMsgRequest);
            for (Message m : receiveMsgResult.getMessages()) {
                ClientResponseMessageWrapper messageWrapper = new ClientResponseMessageWrapper(m.getBody());
                if (messageWrapper.responseOnRequest(jsonClientRequest.getRequestId())) {
                    result = new JsonClientResponse(messageWrapper, jsonClientRequest.getMethod());
                }
            }
        }
        
        if (result == null) {
            LOG.warn("Response wait timeout for request: {}", jsonClientRequest);
            throw new RemoteServiceClientException("Response wait timeout for request id: " + jsonClientRequest.getRequestId());
        }

        return result;
    }

    private boolean untilTimeout(long started) {
        return System.currentTimeMillis() - started > receiveTimeout;
    }

    public long getReceiveTimeout() {
        return receiveTimeout;
    }

    public void setReceiveTimeout(long receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
    }
}
