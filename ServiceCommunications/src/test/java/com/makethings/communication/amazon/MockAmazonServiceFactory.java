package com.makethings.communication.amazon;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.STSSessionCredentialsProvider;

public class MockAmazonServiceFactory implements AmazonServiceFactoty {
    
    private STSSessionCredentialsProvider stsSessionCredentialsProvider;
    
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
    
}
