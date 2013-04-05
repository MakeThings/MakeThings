package com.makethings.communication.amazon;

import com.amazonaws.auth.AWSCredentials;

public class AmazonTempCredentials implements AmazonServiceCredentials {

    private final AWSCredentials credentials;

    public AmazonTempCredentials(AWSCredentials credentials) {
        this.credentials = credentials;
    }

    public String getAccessKeyId() {
        return credentials.getAWSAccessKeyId();
    }

    public String getSecretAccessKey() {
        return credentials.getAWSSecretKey();
    }
}
