package com.makethings.communication.rpc.sqs;

import com.makethings.communication.rpc.ClientManager;
import com.makethings.communication.rpc.RemoteServiceClient;
import com.makethings.communication.session.user.UserSession;
import com.makethings.communication.session.user.UserSessionDefinition;

public class SqsRemoteServiceClient implements RemoteServiceClient {

    private ClientManager clientManager;
    private UserSessionDefinition sessionDefinition;
    private UserSession session;

    public void init() {
        session = clientManager.openClientSession(sessionDefinition);
    }

    public void setClientManaget(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    public void setUserSessionDefinition(UserSessionDefinition sessionDefinition) {
        this.sessionDefinition = sessionDefinition;
    }
    
    

}
