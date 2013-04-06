package com.makethings.communication.session;

public interface ApplicationSessionService {

    ApplicationSession getSessionById(String sessionId);

    ApplicationSession createNewSession(ApplicationSessionDefinition definition);

}
