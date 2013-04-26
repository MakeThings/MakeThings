package com.makethings.communication.session.service;

import com.makethings.communication.queue.QueueServiceCredentials;
import com.makethings.communication.queue.ServiceRequestQueueName;

public class DefaultServiceSession implements ServiceSession {

    private String id;
    private final ServiceRequestQueueName requestQueueName;
    private final ServiceName serviceName;
    private QueueServiceCredentials queueServiceCredentials;

    public DefaultServiceSession(ServiceSessionDefinition sessionDef) {
        requestQueueName = sessionDef.getRequestQueueName();
        serviceName = sessionDef.getServiceName();
    }

    public String getRequstQueueName() {
        return requestQueueName.getName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName.getName();
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
