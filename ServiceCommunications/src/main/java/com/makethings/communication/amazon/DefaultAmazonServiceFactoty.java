package com.makethings.communication.amazon;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.STSSessionCredentialsProvider;

public class DefaultAmazonServiceFactoty implements AmazonServiceFactoty {

    @Override
    public STSSessionCredentialsProvider createStsSessionCredentialsProvider(AWSCredentials awsCredentials) {
        return new STSSessionCredentialsProvider(awsCredentials);
    }

}
