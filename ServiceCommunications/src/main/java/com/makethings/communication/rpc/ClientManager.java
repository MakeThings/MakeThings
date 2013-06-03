package com.makethings.communication.rpc;

import com.makethings.communication.session.user.UserSession;
import com.makethings.communication.session.user.UserSessionDefinition;

public interface ClientManager {

    UserSession openClientSession(UserSessionDefinition same);

    String getServiceRequestQueueName(String remoteServiceName);

}
