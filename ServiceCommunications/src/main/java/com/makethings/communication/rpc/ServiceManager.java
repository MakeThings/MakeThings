package com.makethings.communication.rpc;

import com.makethings.communication.session.CreateSessionException;
import com.makethings.communication.session.SessionNotFoundException;
import com.makethings.communication.session.service.ServiceSession;
import com.makethings.communication.session.service.ServiceSessionDefinition;

public interface ServiceManager {
    ServiceSession openServiceSession(ServiceSessionDefinition sessionDefinition) throws CreateSessionException;

    void closeServiceSession(String sessionId) throws SessionNotFoundException;

    void reportServiceStatus(String sessionId, RemoteServiceState state);

}