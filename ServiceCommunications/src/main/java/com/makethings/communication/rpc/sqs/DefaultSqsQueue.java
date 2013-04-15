package com.makethings.communication.rpc.sqs;

import org.springframework.beans.factory.annotation.Required;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.makethings.communication.amazon.AmazonServiceFactoty;

public class DefaultSqsQueue implements SqsQueue {

    private AmazonServiceFactoty serviceFactory;
    private AWSCredentials credentials;
    private AmazonSQS sqsClient;

    @Override
    public void setAwsCredentials(AWSCredentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public SendMessageResult sendMessage(SendMessageRequest messageRequest) {
        AmazonSQS sqsClient = getSqsClient();
        return sqsClient.sendMessage(messageRequest);
    }
    
    @Override
    public ReceiveMessageResult receiveMessage(ReceiveMessageRequest receiveMessageRequest) {
        AmazonSQS sqsClient = getSqsClient();
        return sqsClient.receiveMessage(receiveMessageRequest);
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
