package com.makethings.communication.session.service;

import org.springframework.beans.factory.annotation.Required;

import com.makethings.communication.queue.ServiceRequestQueueName;
import com.makethings.communication.session.ApplicationSessionDefinition;
import com.makethings.communication.session.ApplicationSessionFactory;

public abstract class ServiceSessionDefinition implements ApplicationSessionDefinition {

    private ServiceRequestQueueName requestQueueName;
    private ServiceName serviceName;

    public ServiceRequestQueueName getRequestQueueName() {
        return requestQueueName;
    }

    @Required
    public void setRequestQueueName(ServiceRequestQueueName requestQueueName) {
        this.requestQueueName = requestQueueName;
    }

    public ServiceName getServiceName() {
        return serviceName;
    }

    @Required
    public void setServiceName(ServiceName serviceName) {
        this.serviceName = serviceName;
    }

    public abstract ApplicationSessionFactory getSessionFactory();

    @Override
    public String toString() {
        return "ServiceSessionDefinition [requestQueueName=" + requestQueueName + ", serviceName=" + serviceName + "]";
    }

}
