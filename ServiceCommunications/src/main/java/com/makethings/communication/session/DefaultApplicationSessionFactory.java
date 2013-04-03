package com.makethings.communication.session;

import com.makethings.communication.session.service.DefaultServiceSession;
import com.makethings.communication.session.service.ServiceSessionDefinition;

public class DefaultApplicationSessionFactory implements ApplicationSessionFactory {

    public ApplicationSession createSession(ServiceSessionDefinition sessionDef) {
        return new DefaultServiceSession(sessionDef);
    }
    
}
