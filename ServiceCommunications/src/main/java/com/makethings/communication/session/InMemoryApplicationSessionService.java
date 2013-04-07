package com.makethings.communication.session;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryApplicationSessionService implements ApplicationSessionService {

    private final static Logger LOG = LoggerFactory.getLogger(InMemoryApplicationSessionService.class);

    private Map<String, ApplicationSession> sessions;

    public InMemoryApplicationSessionService() {
        this.sessions = new HashMap<String, ApplicationSession>();
    }

    public ApplicationSession createNewSession(ApplicationSessionDefinition definition) {

        LOG.debug("Creating session for definition: {}", definition);

        ApplicationSessionFactory sessionFactory = definition.getSessionFactory();
        ApplicationSession session = sessionFactory.createSession(definition);
        sessions.put(session.getId(), session);

        LOG.info("Created session: {} for definition: {}", session, definition);

        return session;
    }

    public ApplicationSession getSessionById(String sessionId) {
        
        LOG.debug("Looking application session by id: {}", sessionId);
        
        ApplicationSession session = sessions.get(sessionId);
        
        if (session == null) {
            LOG.debug("Session with id: {} not found", sessionId);
            throw new SessionNotFoundException("Session with id: " + sessionId + " not found", sessionId);
        }
        
        return session;
    }
}
