package com.makethings.communication.amazon;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.STSSessionCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;

public interface AmazonServiceFactoty {

    STSSessionCredentialsProvider createStsSessionCredentialsProvider(AWSCredentials awsCredentials);
    
    AmazonSQS createAmazonSqs(AWSCredentials awsCredentials);
}
