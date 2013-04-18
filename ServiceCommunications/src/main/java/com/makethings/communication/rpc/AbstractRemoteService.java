package com.makethings.communication.rpc;

import com.makethings.communication.session.service.ServiceSession;
import com.makethings.communication.session.service.ServiceSessionDefinition;

public abstract class AbstractRemoteService implements RemoteService {
    private ServiceSessionDefinition sessionDefinition;
    private ServiceSession serviceSession;
    private RemoteServiceManager serviceManager;
    private boolean inited = false;

    @Override
    public void init() {
        if (!inited) {
            createSession();
            inited = true;
        }
        else {
            throw new RemoteServiceException("Remote service already inited");
        }
    }

    private void createSession() {
        try {
            serviceSession = serviceManager.openServiceSession(sessionDefinition);
        }
        catch (RuntimeException e) {
            throw new RemoteServiceException("Remote service cannot be inited", e);
        }
    }

    @Override
    public ServiceSession getSession() {
        return serviceSession;
    }

    @Override
    public void start() {

    }

    public ServiceSessionDefinition getSessionDefinition() {
        return sessionDefinition;
    }

    public void setSessionDefinition(ServiceSessionDefinition sessionDefinition) {
        this.sessionDefinition = sessionDefinition;
    }

    public RemoteServiceManager getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(RemoteServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

}
