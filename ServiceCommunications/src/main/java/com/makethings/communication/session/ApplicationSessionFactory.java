package com.makethings.communication.session;

import com.makethings.communication.session.service.ServiceSessionDefinition;

public interface ApplicationSessionFactory {

    ApplicationSession createSession(ServiceSessionDefinition sessionDef);

}
