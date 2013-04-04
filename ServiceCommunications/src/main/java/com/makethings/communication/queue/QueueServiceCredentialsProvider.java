package com.makethings.communication.queue;

public interface QueueServiceCredentialsProvider<C extends QueueServiceCredentials> {

    C getCredentials();

}
