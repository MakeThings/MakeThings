package com.makethings.communication.rpc;


import com.makethings.communication.session.ApplicationSession;
import com.makethings.communication.session.ApplicationSessionService;
import com.makethings.communication.session.service.ServiceSession;
import com.makethings.communication.session.service.ServiceSessionDefinition;

public class DefaultRemoteServiceManager implements RemoteServiceManager {
    
    private static final long serialVersionUID = 7783623048214943701L;
    
    private ApplicationSessionService applicationSessionService;

    public ApplicationSessionService getApplicationSessionService() {
        return applicationSessionService;
    }

    public void setApplicationSessionService(ApplicationSessionService applicationSessionService) {
        this.applicationSessionService = applicationSessionService;
    }

    @Override
    public ServiceSession openServiceSession(ServiceSessionDefinition definition) {
        ApplicationSession applicationSession = applicationSessionService.createNewSession(definition);
        if (applicationSession instanceof ServiceSession) {
            return (ServiceSession)applicationSession;
        }
        return null;
    }

}
