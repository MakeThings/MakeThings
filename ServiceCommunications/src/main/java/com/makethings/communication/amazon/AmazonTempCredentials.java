package com.makethings.communication.amazon;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.STSSessionCredentialsProvider;

public class AmazonTempCredentials implements AmazonServiceCredentials {

    private final STSSessionCredentialsProvider provider;

    public AmazonTempCredentials(STSSessionCredentialsProvider provider) {
        this.provider = provider;
    }

    @Override
    public AWSCredentials getAwsCredentials() {
        return provider.getCredentials();
    }

    @Override
    public String toString() {
        return "AmazonTempCredentials [provider=" + provider + "]";
    }
    
}
