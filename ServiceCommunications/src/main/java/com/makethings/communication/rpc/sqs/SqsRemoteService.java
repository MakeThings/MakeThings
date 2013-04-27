package com.makethings.communication.rpc.sqs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.makethings.communication.amazon.AmazonServiceCredentials;
import com.makethings.communication.queue.QueueServiceCredentials;
import com.makethings.communication.rpc.AbstractRemoteService;
import com.makethings.communication.session.service.ServiceSession;

public class SqsRemoteService extends AbstractRemoteService {

    private final static Logger LOG = LoggerFactory.getLogger(SqsRemoteService.class);

    private SqsQueue queue;

    private ReceiveMessageRequest request;

    @Override
    protected void onInit() {
        super.onInit();
        
        LOG.debug("Setting amazon sqs creadentials to a queue");
        QueueServiceCredentials queueServiceCredentials = getSession().getQueueServiceCredentials();
        AmazonServiceCredentials amazonSqsCredentials = (AmazonServiceCredentials)queueServiceCredentials; 
        queue.setAwsCredentials(amazonSqsCredentials.getAwsCredentials());
    }

    @Override
    protected void startProcessing() {
        ServiceSession session = getSession();
        String queueName = session.getRequstQueueName();
        
        request = new ReceiveMessageRequest().withQueueUrl(queueName);

        ReceiveMessageResult receivedMessages = queue.receiveMessage(request);
    }

    public SqsQueue getQueue() {
        return queue;
    }

    public void setQueue(SqsQueue queue) {
        this.queue = queue;
    }
}
