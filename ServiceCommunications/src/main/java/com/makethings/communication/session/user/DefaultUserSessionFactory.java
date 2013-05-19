package com.makethings.communication.session.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.makethings.communication.session.ApplicationSession;
import com.makethings.communication.session.ApplicationSessionDefinition;
import com.makethings.communication.session.ApplicationSessionFactory;

public class DefaultUserSessionFactory implements ApplicationSessionFactory {

    private final static Logger LOG = LoggerFactory.getLogger(DefaultUserSessionFactory.class);

    @Override
    public ApplicationSession createSession(ApplicationSessionDefinition sessionDef) {

        LOG.debug("Creating user session by definition {}", sessionDef);
        DefaultUserSession session = new DefaultUserSession();
        UserSessionDefinition userSessionDefinition = (UserSessionDefinition) sessionDef;
        session.setResponseQueueName(userSessionDefinition.getClientResponseQueueName().getClientResponseQueueName());
        return session;
    }
}
