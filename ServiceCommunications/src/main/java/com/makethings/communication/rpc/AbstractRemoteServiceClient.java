package com.makethings.communication.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.makethings.communication.rpc.json.JsonClientMarshaler;
import com.makethings.communication.session.user.UserSession;
import com.makethings.communication.session.user.UserSessionDefinition;

public abstract class AbstractRemoteServiceClient implements RemoteServiceClient {
    
    private final static Logger LOG = LoggerFactory.getLogger(AbstractRemoteServiceClient.class);
    
    protected ClientManager clientManager;
    protected UserSessionDefinition sessionDefinition;
    protected UserSession session;
    protected String remoteServiceName;
    protected String requestQueueName;
    protected JsonClientMarshaler jsonClientMarshaler;
    
    
    public final void init() {
        LOG.info("Initialising Remote Service Client");
        session = clientManager.openClientSession(sessionDefinition);
        LOG.info("Created user session id: {}", session.getId());
        requestQueueName = clientManager.getServiceRequestQueueName(remoteServiceName);
        
        onInit();
        
        LOG.info("Remote Service Client for session: {} is created", session.getId());
    }
    
    protected void onInit() {
        
    }

    public void setClientManaget(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    public void setUserSessionDefinition(UserSessionDefinition sessionDefinition) {
        this.sessionDefinition = sessionDefinition;
    }

    public void setRemoteServiceName(String remoteServiceName) {
        this.remoteServiceName = remoteServiceName;
    }

    public UserSession getUserSession() {
        return session;
    }
    
    public String getRequestQueueName() {
        return requestQueueName;
    }
    
    public JsonClientMarshaler getJsonClientMarshaler() {
        return jsonClientMarshaler;
    }

    public void setJsonClientMarshaler(JsonClientMarshaler jsonClientMarshaler) {
        this.jsonClientMarshaler = jsonClientMarshaler;
    }
}
