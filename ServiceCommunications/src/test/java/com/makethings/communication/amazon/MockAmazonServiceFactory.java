package com.makethings.communication.amazon;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.STSSessionCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;

public class MockAmazonServiceFactory implements AmazonServiceFactoty {

    private STSSessionCredentialsProvider stsSessionCredentialsProvider;
    private AmazonSQS amazonSQS;

    @Override
    public STSSessionCredentialsProvider createStsSessionCredentialsProvider(AWSCredentials awsCredentials) {
        return stsSessionCredentialsProvider;
    }

    public STSSessionCredentialsProvider getStsSessionCredentialsProvider() {
        return stsSessionCredentialsProvider;
    }

    public void setStsSessionCredentialsProvider(STSSessionCredentialsProvider stsSessionCredentialsProvider) {
        this.stsSessionCredentialsProvider = stsSessionCredentialsProvider;
    }

    @Override
    public AmazonSQS createAmazonSqs(AWSCredentials awsCredentials) {
        return amazonSQS;
    }

    public AmazonSQS getAmazonSqs() {
        return amazonSQS;
    }

    public void setAmazonSqs(AmazonSQS amazonSQS) {
        this.amazonSQS = amazonSQS;
    }

}
