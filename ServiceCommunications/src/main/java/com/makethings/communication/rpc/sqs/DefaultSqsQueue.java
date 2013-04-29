package com.makethings.communication.rpc.sqs;

import org.springframework.beans.factory.annotation.Required;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.makethings.communication.amazon.AmazonServiceFactoty;
import com.makethings.communication.rpc.QueueException;

public class DefaultSqsQueue implements SqsQueue {

    private AmazonServiceFactoty serviceFactory;
    private AWSCredentials credentials;
    private AmazonSQS sqsClient;

    @Override
    public void setAwsCredentials(AWSCredentials credentials) {
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

    public AmazonServiceFactoty getServiceFactoty() {
        return serviceFactory;
    }

    @Required
    public void setServiceFactoty(AmazonServiceFactoty serviceFactoty) {
        this.serviceFactory = serviceFactoty;
    }

}
