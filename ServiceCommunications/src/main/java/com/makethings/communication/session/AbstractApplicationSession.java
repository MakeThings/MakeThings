package com.makethings.communication.session;

import com.makethings.communication.queue.QueueServiceCredentials;

public abstract class AbstractApplicationSession implements ApplicationSession {

    private String id;
    private QueueServiceCredentials queueServiceCredentials;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public QueueServiceCredentials getQueueServiceCredentials() {
        return queueServiceCredentials;
    }

    public void setQueueServiceCredentials(QueueServiceCredentials credentials) {
        this.queueServiceCredentials = credentials;
    }

}
