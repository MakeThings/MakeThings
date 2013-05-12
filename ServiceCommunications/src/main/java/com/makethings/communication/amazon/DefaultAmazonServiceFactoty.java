package com.makethings.communication.amazon;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.STSSessionCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;

public class DefaultAmazonServiceFactoty implements AmazonServiceFactoty {

    @Override
    public STSSessionCredentialsProvider createStsSessionCredentialsProvider(AWSCredentials awsCredentials)
            throws AmazonServiceFactoryException {
        return new STSSessionCredentialsProvider(awsCredentials);
    }

    @Override
    public AmazonSQS createAmazonSqs(AWSCredentials awsCredentials) throws AmazonServiceFactoryException {
        return new AmazonSQSClient(awsCredentials);
    }

}
