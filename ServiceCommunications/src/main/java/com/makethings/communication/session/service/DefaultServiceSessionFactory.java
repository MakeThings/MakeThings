package com.makethings.communication.session.service;

import org.springframework.beans.factory.annotation.Required;

import com.makethings.communication.session.ApplicationSession;
import com.makethings.communication.session.ApplicationSessionFactory;
import com.makethings.communication.session.SessionIdProvider;

public class DefaultServiceSessionFactory implements ApplicationSessionFactory {

    private SessionIdProvider sessionIdProvider;
    
    public ApplicationSession createSession(ServiceSessionDefinition sessionDef) {
        DefaultServiceSession session = new DefaultServiceSession(sessionDef);
        session.setId(sessionIdProvider.getSessionId());
        return session;
    }

    public SessionIdProvider getSessionIdProvider() {
        return sessionIdProvider;
    }

    @Required
    public void setSessionIdProvider(SessionIdProvider sessionIdProvider) {
        this.sessionIdProvider = sessionIdProvider;
    }

}
