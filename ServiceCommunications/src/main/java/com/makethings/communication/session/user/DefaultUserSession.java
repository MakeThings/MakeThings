package com.makethings.communication.session.user;

import com.makethings.communication.session.AbstractApplicationSession;

public class DefaultUserSession extends AbstractApplicationSession implements UserSession {

    private String responseQueueName;
    private ClientType clientType;

    @Override
    public String getResponseQueueName() {
        return responseQueueName;
    }

    public void setResponseQueueName(String responseQueueName) {
        this.responseQueueName = responseQueueName;
    }

    @Override
    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }
    

}
