package com.makethings.communication.amazon;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Required;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.STSSessionCredentialsProvider;
import com.makethings.communication.queue.QueueServiceCredentialsProvider;

public class AmazonSTSCredentialsProvider implements QueueServiceCredentialsProvider<AmazonTempCredentials> {

    private AWSCredentials awsCred;
    private AmazonServiceFactoty amazonServiceFactoty;
    private STSSessionCredentialsProvider provider;

    @PostConstruct
    public void init() {
        provider = amazonServiceFactoty.createStsSessionCredentialsProvider(awsCred);
    }

    public AmazonTempCredentials getCredentials() {
        return new AmazonTempCredentials(provider);
    }

    @Required
    public void setAwsCredentials(AWSCredentials awsCred) {
        this.awsCred = awsCred;
    }

    @Required
    public void setAmazonServiceFactory(AmazonServiceFactoty amazonServiceFactoty) {
        this.amazonServiceFactoty = amazonServiceFactoty;
    }

}
