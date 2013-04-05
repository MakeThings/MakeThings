package com.makethings.communication.amazon;

import com.makethings.communication.queue.QueueServiceCredentials;

public interface AmazonServiceCredentials extends QueueServiceCredentials {

    String getAccessKeyId();

    String getSecretAccessKey();

}
