package com.makethings.communication.session.service;

import com.makethings.communication.queue.QueueServiceCredentials;

public class DefaultServiceSession implements ServiceSession {

    private String id;
    private String requestQueueName;
    private String serviceName;
    private QueueServiceCredentials queueServiceCredentials;

    public String getRequestQueueName() {
        return requestQueueName;
    }

    public void setRequestQueueName(String requestQueueName) {
        this.requestQueueName = requestQueueName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public QueueServiceCredentials getQueueServiceCredentials() {
        return queueServiceCredentials;
    }

    public void setQueueServiceCredentials(QueueServiceCredentials credentials) {
        this.queueServiceCredentials = credentials;
    }

    @Override
    public String toString() {
        return "DefaultServiceSession [id=" + id + ", requestQueueName=" + requestQueueName + ", serviceName=" + serviceName + "]";
    }
  
}
