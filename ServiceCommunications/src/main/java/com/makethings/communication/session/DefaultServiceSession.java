package com.makethings.communication.session;

public class DefaultServiceSession implements ServiceSession {

    private final ServiceSessionDefinition sessionDef;

    public DefaultServiceSession(ServiceSessionDefinition sessionDef) {
        this.sessionDef = sessionDef;
    }

    public String getRequstQueueName() {
        return sessionDef.getRequestQueueName();
    }

}
