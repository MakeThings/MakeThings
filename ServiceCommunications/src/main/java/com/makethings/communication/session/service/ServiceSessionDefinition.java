package com.makethings.communication.session.service;

import com.makethings.communication.session.ApplicationSessionDefinition;
import com.makethings.communication.session.ApplicationSessionFactory;
import com.makethings.communication.session.SessionIdProvider;

public abstract class ServiceSessionDefinition implements ApplicationSessionDefinition {

    private String requestQueueName;

    public String getRequestQueueName() {
        return requestQueueName;
    }

    public void setRequestQueueName(String requestQueueName) {
        this.requestQueueName = requestQueueName;
    }

    public abstract ApplicationSessionFactory getSessionFactory();

}
