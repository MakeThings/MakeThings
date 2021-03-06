package com.makethings.communication.session.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.makethings.communication.queue.QueueServiceCredentials;
import com.makethings.communication.queue.QueueServiceCredentialsProvider;
import com.makethings.communication.session.ApplicationSession;
import com.makethings.communication.session.ApplicationSessionDefinition;
import com.makethings.communication.session.ApplicationSessionFactory;
import com.makethings.communication.session.SessionIdProvider;

public class DefaultUserSessionFactory implements ApplicationSessionFactory {

    private final static Logger LOG = LoggerFactory.getLogger(DefaultUserSessionFactory.class);

    private SessionIdProvider sessionIdProvider;
    private QueueServiceCredentialsProvider<? extends QueueServiceCredentials> queueServiceCredentialsProvider;

    public static Logger getLog() {
        return LOG;
    }

    @Override
    public ApplicationSession createSession(ApplicationSessionDefinition sessionDef) {

        LOG.debug("Creating user session by definition {}", sessionDef);
        DefaultUserSession session = new DefaultUserSession();
        UserSessionDefinition userSessionDefinition = (UserSessionDefinition) sessionDef;
        session.setResponseQueueName(userSessionDefinition.getClientResponseQueueName().getClientResponseQueueName());
        session.setClientType(userSessionDefinition.getClientType());
        session.setId(sessionIdProvider.getSessionId());
        session.setQueueServiceCredentials(queueServiceCredentialsProvider.getCredentials());
        return session;
    }

    public SessionIdProvider getSessionIdProvider() {
        return sessionIdProvider;
    }

    public void setSessionIdProvider(SessionIdProvider sessionIdProvider) {
        this.sessionIdProvider = sessionIdProvider;
    }

    public QueueServiceCredentialsProvider<? extends QueueServiceCredentials> getQueueServiceCredentialsProvider() {
        return queueServiceCredentialsProvider;
    }

    public void setQueueServiceCredentialsProvider(
            QueueServiceCredentialsProvider<? extends QueueServiceCredentials> queueServiceCredentialsProvider) {
        this.queueServiceCredentialsProvider = queueServiceCredentialsProvider;
    }

}
