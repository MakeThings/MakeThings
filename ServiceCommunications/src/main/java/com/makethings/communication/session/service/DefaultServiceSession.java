package com.makethings.communication.session.service;

import com.makethings.communication.session.AbstractApplicationSession;

public class DefaultServiceSession extends AbstractApplicationSession implements ServiceSession {

    private String requestQueueName;
    private String serviceName;

    public String getRequestQueueName() {
        return requestQueueName;
    }

    public void setRequestQueueName(String requestQueueName) {
        this.requestQueueName = requestQueueName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "DefaultServiceSession [id=" + getId() + ", requestQueueName=" + requestQueueName + ", serviceName=" + serviceName + "]";
    }

}
