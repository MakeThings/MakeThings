package com.makethings.communication.amazon;

import org.springframework.beans.factory.annotation.Required;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.auth.STSSessionCredentialsProvider;
import com.makethings.communication.queue.QueueServiceCredentialsProvider;

public class AmazonSTSCredentialsProvider implements QueueServiceCredentialsProvider<AmazonTempCredentials> {

    private STSSessionCredentialsProvider provider;

    public AmazonTempCredentials getCredentials() {
        return new AmazonTempCredentials(provider.getCredentials());
    }

    @Required
    public void setAwsCredentials(AWSCredentials awsCred) {
        provider = new STSSessionCredentialsProvider(awsCred);
    }

}
