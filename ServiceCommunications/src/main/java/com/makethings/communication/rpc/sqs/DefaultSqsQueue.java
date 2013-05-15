package com.makethings.communication.rpc.sqs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.makethings.communication.amazon.AmazonServiceFactoty;
import com.makethings.communication.rpc.QueueException;

public class DefaultSqsQueue implements SqsQueue {

    private final static Logger LOG = LoggerFactory.getLogger(DefaultSqsQueue.class);

    private AmazonServiceFactoty serviceFactory;
    private AWSCredentials credentials;
    private AmazonSQS sqsClient;

    @Override
    public void setAwsCredentials(AWSCredentials credentials) {
        LOG.info("Populating AWS Credentials(AWSAccessKeyId: {})  to queue", credentials.getAWSAccessKeyId());
        this.credentials = credentials;
    }

    @Override
    public SendMessageResult sendMessage(SendMessageRequest messageRequest) throws QueueException {
        AmazonSQS sqsClient = getSqsClient();
        try {
            return sqsClient.sendMessage(messageRequest);
        }
        catch (RuntimeException e) {
            throw new QueueException("Error when send message tho queue" + messageRequest.getQueueUrl(), e);
        }
    }

    @Override
    public ReceiveMessageResult receiveMessage(ReceiveMessageRequest receiveMessageRequest) throws QueueException {
        AmazonSQS sqsClient = getSqsClient();
        try {
            return sqsClient.receiveMessage(receiveMessageRequest);
        }
        catch (RuntimeException e) {
            throw new QueueException("Error when receive message from queue" + receiveMessageRequest.getQueueUrl(), e);
        }
    }

    @Override
    public void deleteMessage(DeleteMessageRequest deleteMessageRequest) {
        AmazonSQS sqsClient = getSqsClient();
        try {
            sqsClient.deleteMessage(deleteMessageRequest);
        }
        catch (RuntimeException e) {
            throw new QueueException("Error when delete message from queue" + deleteMessageRequest.getQueueUrl(), e);
        }
    }

    private AmazonSQS getSqsClient() {
        if (sqsClient == null) {
            sqsClient = serviceFactory.createAmazonSqs(credentials);
        }
        return sqsClient;
    }

    @Override
    public CreateQueueResult createQueue(CreateQueueRequest request) throws QueueException {
        try {
            return getSqsClient().createQueue(request);
        }
        catch (RuntimeException e) {
            throw new QueueException("Error when create queue: " + request.toString(), e);
        }
    }

    public AmazonServiceFactoty getServiceFactoty() {
        return serviceFactory;
    }

    @Required
    public void setServiceFactoty(AmazonServiceFactoty serviceFactoty) {
        this.serviceFactory = serviceFactoty;
    }

}
