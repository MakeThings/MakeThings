package com.makethings.communication.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.makethings.communication.session.ApplicationSession;
import com.makethings.communication.session.ApplicationSessionService;
import com.makethings.communication.session.CreateSessionException;
import com.makethings.communication.session.SessionException;
import com.makethings.communication.session.SessionNotFoundException;
import com.makethings.communication.session.service.ServiceSession;
import com.makethings.communication.session.service.ServiceSessionDefinition;
import com.makethings.communication.session.user.UserSession;

public class DefaultServiceManager implements ServiceManager {

    private final static Logger LOG = LoggerFactory.getLogger(DefaultServiceManager.class);

    private ApplicationSessionService sessionService;

    @Override
    public ServiceSession openServiceSession(ServiceSessionDefinition sessionDefinition) throws CreateSessionException {
        LOG.info("Creating service session for: {}", sessionDefinition.getServiceName());

        ApplicationSession appSession = sessionService.createNewSession(sessionDefinition);
        ServiceSession serviceSession = null;
        if (appSession instanceof ServiceSession) {
            serviceSession = (ServiceSession) appSession;
        }
        else {
            throw new CreateSessionException("Created session has worong type: " + appSession.getClass());
        }
        return serviceSession;
    }

    @Override
    public void closeServiceSession(String sessionId) throws SessionNotFoundException {
        LOG.info("Closing service session: {}", sessionId);

        sessionService.deleteSessionById(sessionId);
    }

    @Override
    public void reportServiceStatus(String sessionId, RemoteServiceState state) {
        // not implemented yet
    }

    @Override
    public String getClientResponseQueueName(String clientSessionId) {
        ApplicationSession appSession = sessionService.getSessionById(clientSessionId);
        if (appSession instanceof UserSession) {
            return ((UserSession) appSession).getResponseQueueName();
        }
        else {
            throw new SessionException("Wrong session returned when getting client response queue name, sessionId: " + clientSessionId);
        }
    }

    public ApplicationSessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(ApplicationSessionService sessionService) {
        this.sessionService = sessionService;
    }

}
