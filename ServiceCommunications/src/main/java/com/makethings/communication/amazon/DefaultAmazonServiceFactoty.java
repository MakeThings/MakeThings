package com.makethings.communication.amazon;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.STSSessionCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;

public class DefaultAmazonServiceFactoty implements AmazonServiceFactoty {

    @Override
    public STSSessionCredentialsProvider createStsSessionCredentialsProvider(AWSCredentials awsCredentials) {
        return new STSSessionCredentialsProvider(awsCredentials);
    }

    @Override
    public AmazonSQS createAmazonSqs(AWSCredentials awsCredentials) {
        return null;
    }

}