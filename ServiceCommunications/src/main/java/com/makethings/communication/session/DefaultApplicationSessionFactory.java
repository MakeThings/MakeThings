package com.makethings.communication.session;

public class DefaultApplicationSessionFactory implements ApplicationSessionFactory {

    public ApplicationSession createSession(ServiceSessionDefinition sessionDef) {
        return new DefaultServiceSession(sessionDef);
    }
    
}
