package com.makethings.communication.rpc.sqs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.makethings.communication.rpc.ClientManager;
import com.makethings.communication.rpc.RemoteServiceClient;
import com.makethings.communication.session.user.UserSession;
import com.makethings.communication.session.user.UserSessionDefinition;

public class SqsRemoteServiceClient implements RemoteServiceClient {

    private final static Logger LOG = LoggerFactory.getLogger(SqsRemoteServiceClient.class);
    
    private ClientManager clientManager;
    private UserSessionDefinition sessionDefinition;
    private UserSession session;
    private String remoteServiceName;
    private String requestQueueName;
    private SqsQueue queue;

    public void init() {
        LOG.info("Initialising Remote Service Client");
        session = clientManager.openClientSession(sessionDefinition);
        LOG.info("Created user session id: {}", session.getId());
        requestQueueName = clientManager.getServiceRequestQueueName(remoteServiceName);
        queue.createQueue(new CreateQueueRequest(session.getResponseQueueName()));
        LOG.info("Remote Service Client for session: {} is created", session.getId());
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

    public void setQueue(SqsQueue queue) {
        this.queue = queue;

    }
}
