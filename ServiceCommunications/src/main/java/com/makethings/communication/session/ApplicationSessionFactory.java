package com.makethings.communication.session;

public interface ApplicationSessionFactory {

    ApplicationSession createSession(ServiceSessionDefinition sessionDef);

}
