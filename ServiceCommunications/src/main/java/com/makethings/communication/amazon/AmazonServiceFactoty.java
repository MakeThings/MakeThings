package com.makethings.communication.amazon;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.STSSessionCredentialsProvider;

public interface AmazonServiceFactoty {

    STSSessionCredentialsProvider createStsSessionCredentialsProvider(AWSCredentials awsCredentials);
}
