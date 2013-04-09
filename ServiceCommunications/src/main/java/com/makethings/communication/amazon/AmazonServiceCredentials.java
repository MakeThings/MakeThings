package com.makethings.communication.amazon;

import com.amazonaws.auth.AWSCredentials;
import com.makethings.communication.queue.QueueServiceCredentials;

public interface AmazonServiceCredentials extends QueueServiceCredentials {

    AWSCredentials getAwsCredentials();

}
