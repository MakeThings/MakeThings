package com.makethings.communication.rpc;

import com.makethings.communication.session.ApplicationSession;
import com.makethings.communication.session.ApplicationSessionService;
import com.makethings.communication.session.CreateSessionException;
import com.makethings.communication.session.SessionNotFoundException;
import com.makethings.communication.session.service.ServiceSession;
import com.makethings.communication.session.service.ServiceSessionDefinition;

public class DefaultServiceManager implements ServiceManager {

    private ApplicationSessionService sessionService;

    @Override
    public ServiceSession openServiceSession(ServiceSessionDefinition sessionDefinition) throws CreateSessionException {
        ApplicationSession appSession = sessionService.createNewSession(sessionDefinition);
        ServiceSession serviceSession = null;
        if (appSession instanceof ServiceSession) {
            serviceSession = (ServiceSession) appSession;
        }
        return serviceSession;
    }

    @Override
    public void closeServiceSession(String sessionId) throws SessionNotFoundException {
        // TODO Auto-generated method stub

    }

    @Override
    public void reportServiceStatus(String sessionId, RemoteServiceState state) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getClientResponseQueueName(String clientSessionId) {
        // TODO Auto-generated method stub
        return null;
    }

    public ApplicationSessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(ApplicationSessionService sessionService) {
        this.sessionService = sessionService;
    }
    
    

}
