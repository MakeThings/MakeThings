package com.makethings.communication.session.user;

import org.springframework.core.style.ToStringCreator;

import com.makethings.communication.queue.ClientResponseQueueName;
import com.makethings.communication.session.ApplicationSessionFactory;

public class DefaultUserSessionDefinition implements UserSessionDefinition {

    private ClientResponseQueueName responseQueueName;

    private DefaultUserSessionFactory userSesssionFactory;

    private ClientType clientType;

    @Override
    public ApplicationSessionFactory getSessionFactory() {
        return userSesssionFactory;
    }

    public void setClientResponseQueueName(ClientResponseQueueName responseQueueName) {
        this.responseQueueName = responseQueueName;
    }

    public ClientResponseQueueName getClientResponseQueueName() {
        return responseQueueName;
    }

    public DefaultUserSessionFactory getUserSesssionFactory() {
        return userSesssionFactory;
    }

    public void setUserSesssionFactory(DefaultUserSessionFactory userSesssionFactory) {
        this.userSesssionFactory = userSesssionFactory;
    }

    @Override
    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this).append("responseQueueName", responseQueueName.getClientResponseQueueName()).append("clientType", clientType).toString();
    }

}
