package com.makethings.communication.session;

import com.makethings.communication.queue.QueueServiceCredentials;

public interface ApplicationSession {
    /**
     * @return Application Session Id
     */
    String getId();

    /**
     * @return Credentials to connect to remote Queue Service
     */
    QueueServiceCredentials getQueueServiceCredentials();
}
