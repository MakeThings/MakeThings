package com.makethings.communication.amazon;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.STSSessionCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;

public interface AmazonServiceFactoty {

    STSSessionCredentialsProvider createStsSessionCredentialsProvider(AWSCredentials awsCredentials) throws AmazonServiceFactoryException;

    AmazonSQS createAmazonSqs(AWSCredentials awsCredentials) throws AmazonServiceFactoryException;
}
