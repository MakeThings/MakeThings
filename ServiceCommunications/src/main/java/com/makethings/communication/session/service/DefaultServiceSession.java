package com.makethings.communication.session.service;

import com.makethings.communication.queue.ServiceRequestQueueName;

public class DefaultServiceSession implements ServiceSession {

    private String id;
    private final ServiceRequestQueueName requestQueueName;

    public DefaultServiceSession(ServiceSessionDefinition sessionDef) {
        requestQueueName = sessionDef.getRequestQueueName();
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

}
