package com.makethings.communication.rpc.sqs;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.makethings.communication.amazon.AmazonServiceCredentials;
import com.makethings.communication.queue.QueueServiceCredentials;
import com.makethings.communication.rpc.AbstractRemoteService;
import com.makethings.communication.rpc.json.JsonRpcHandler;
import com.makethings.communication.session.service.ServiceSession;

public class SqsRemoteService extends AbstractRemoteService {

    private final static Logger LOG = LoggerFactory.getLogger(SqsRemoteService.class);

    private SqsQueue queue;

    private ReceiveMessageRequest request;

    private Executor requestProcessingExecutor;

    private JsonRpcHandler jsonRpcHandler;

    public SqsRemoteService() {
        requestProcessingExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    protected void onInit() {
        super.onInit();

        LOG.debug("Setting amazon sqs creadentials to a queue");
        QueueServiceCredentials queueServiceCredentials = getSession().getQueueServiceCredentials();
        AmazonServiceCredentials amazonSqsCredentials = (AmazonServiceCredentials) queueServiceCredentials;
        queue.setAwsCredentials(amazonSqsCredentials.getAwsCredentials());
    }

    @Override
    protected void processing() {
        ServiceSession session = getSession();
        String queueName = session.getRequstQueueName();

        request = new ReceiveMessageRequest().withQueueUrl(queueName);

        handleMessages(queue.receiveMessage(request));
    }

    protected void handleMessages(ReceiveMessageResult receivedMessages) {
        List<Message> messages = receivedMessages.getMessages();

        for (Message message : messages) {
            LOG.debug("Handling message {}, received from {}", message, getSession().getRequstQueueName());
            new RequestHandlingTask().withMessage(message).withHandler(jsonRpcHandler).execute(requestProcessingExecutor);
        }
    }

    public SqsQueue getQueue() {
        return queue;
    }

    public void setQueue(SqsQueue queue) {
        this.queue = queue;
    }

    public JsonRpcHandler getJsonRpcHandler() {
        return jsonRpcHandler;
    }

    public void setJsonRpcHandler(JsonRpcHandler jsonRpcHandler) {
        this.jsonRpcHandler = jsonRpcHandler;
    }
}
