package com.makethings.communication.session.service;


public class DefaultServiceSession implements ServiceSession {

    private String id;
    
    private final ServiceSessionDefinition sessionDef;

    public DefaultServiceSession(ServiceSessionDefinition sessionDef) {
        this.sessionDef = sessionDef;
    }

    public String getRequstQueueName() {
        return sessionDef.getRequestQueueName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
