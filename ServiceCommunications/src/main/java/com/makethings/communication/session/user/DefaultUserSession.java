package com.makethings.communication.session.user;

import com.makethings.communication.queue.QueueServiceCredentials;

public class DefaultUserSession implements UserSession {

    private String responseQueueName;
    private ClientType clientType;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public QueueServiceCredentials getQueueServiceCredentials() {
        // TODO Auto-generated method stub
        return null;
    }

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
