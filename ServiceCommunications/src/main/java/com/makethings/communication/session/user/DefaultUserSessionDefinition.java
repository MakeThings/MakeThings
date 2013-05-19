package com.makethings.communication.session.user;

import com.makethings.communication.queue.ClientResponseQueueName;
import com.makethings.communication.session.ApplicationSessionFactory;

public class DefaultUserSessionDefinition implements UserSessionDefinition {

    private ClientResponseQueueName responseQueueName;

    private DefaultUserSessionFactory userSesssionFactory;
    
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
    public String toString() {
        return "DefaultUserSessionDefinition [responseQueueName=" + responseQueueName + "]";
    }
}
