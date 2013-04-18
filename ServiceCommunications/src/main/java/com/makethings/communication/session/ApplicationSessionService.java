package com.makethings.communication.session;

public interface ApplicationSessionService {

    ApplicationSession getSessionById(String sessionId) throws SessionNotFoundException;

    ApplicationSession createNewSession(ApplicationSessionDefinition definition) throws CreateSessionException;

    void deleteSessionById(String sessionId);

}
