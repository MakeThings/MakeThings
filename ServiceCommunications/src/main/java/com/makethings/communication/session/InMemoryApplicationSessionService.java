package com.makethings.communication.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryApplicationSessionService implements ApplicationSessionService {

    private final static Logger LOG = LoggerFactory.getLogger(InMemoryApplicationSessionService.class);
    
    public ApplicationSession createNewSession(ApplicationSessionDefinition definition) {
    
        LOG.info("Creating session for definition {}", definition);
        
        ApplicationSessionFactory sessionFactory = definition.getSessionFactory();
        ApplicationSession session = sessionFactory.createSession(definition);
        
        LOG.info("Created session {} for definition {}", session, definition);
        
        return session;
    }

}
