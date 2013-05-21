package com.makethings.communication.rpc.sqs;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.sqs.model.CreateQueueRequest;
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

    private Executor requestProcessingExecutor;

    private JsonRpcHandler jsonRpcHandler;

    private boolean createQueueOnStartup = false;

    private int waitForMessagesSeconds = 2;

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

        if (createQueueOnStartup) {
            LOG.info("Creating service request queue: {} on startup", getSession().getRequestQueueName());
            queue.createQueue(new CreateQueueRequest().withQueueName(getSession().getRequestQueueName()));
        }
    }

    @Override
    protected void processing() {
        ServiceSession session = getSession();
        String queueName = session.getRequestQueueName();

        ReceiveMessageRequest request = new ReceiveMessageRequest().withQueueUrl(queueName).withWaitTimeSeconds(waitForMessagesSeconds);

        LOG.info("Service {}, start reading messages, request: {}", getServiceName(), request);
        while (isWorking() && !isStopSignalReceived()) {
            handleMessages(queue.receiveMessage(request));
        }
    }

    protected void handleMessages(ReceiveMessageResult receivedMessages) {
        List<Message> messages = receivedMessages.getMessages();
        LOG.debug("Recevied messages {}, from {}", receivedMessages, getSession().getRequestQueueName());
        for (Message message : messages) {
            runMessageHandingTask(message);
        }
    }

    private void runMessageHandingTask(Message m) {
        LOG.debug("Executing handler task for message {}", m);
        new RequestHandlingTask().withMessage(m).withHandler(jsonRpcHandler).withQueue(queue)
                .withRequestQueueName(getSession().getRequestQueueName()).withServiceManager(getServiceManager())
                .execute(requestProcessingExecutor);
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

    public void setCreateQueueOnStartup(boolean createQueueOnStartup) {
        this.createQueueOnStartup = createQueueOnStartup;

    }
}
