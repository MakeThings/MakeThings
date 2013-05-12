package com.makethings.communication.session.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.makethings.communication.queue.QueueServiceCredentials;
import com.makethings.communication.queue.QueueServiceCredentialsProvider;
import com.makethings.communication.session.ApplicationSession;
import com.makethings.communication.session.ApplicationSessionDefinition;
import com.makethings.communication.session.ApplicationSessionFactory;
import com.makethings.communication.session.SessionIdProvider;

public class DefaultServiceSessionFactory implements ApplicationSessionFactory {

    private final static Logger LOG = LoggerFactory.getLogger(DefaultServiceSessionFactory.class);

    private SessionIdProvider sessionIdProvider;
    private QueueServiceCredentialsProvider<? extends QueueServiceCredentials> queueServiceCredentialsProvider;

    public ApplicationSession createSession(ApplicationSessionDefinition sessionDef) {
        DefaultServiceSession session = new DefaultServiceSession((ServiceSessionDefinition) sessionDef);
        session.setId(sessionIdProvider.getSessionId());
        QueueServiceCredentials queueServiceCredentials = queueServiceCredentialsProvider.getCredentials();
        LOG.info("Populating queue service credentials: {} to session: {}", queueServiceCredentials, session);
        session.setQueueServiceCredentials(queueServiceCredentials);
        return session;
    }

    public SessionIdProvider getSessionIdProvider() {
        return sessionIdProvider;
    }

    @Required
    public void setSessionIdProvider(SessionIdProvider sessionIdProvider) {
        this.sessionIdProvider = sessionIdProvider;
    }

    public QueueServiceCredentialsProvider<? extends QueueServiceCredentials> getQueueServiceCredentialsProvider() {
        return queueServiceCredentialsProvider;
    }

    @Required
    public void setQueueServiceCredentialsProvider(
            QueueServiceCredentialsProvider<? extends QueueServiceCredentials> queueServiceCredentialsProvider) {
        this.queueServiceCredentialsProvider = queueServiceCredentialsProvider;
    }

}
