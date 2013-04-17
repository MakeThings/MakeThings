package com.makethings.communication.rpc;

import com.makethings.communication.session.service.ServiceSession;
import com.makethings.communication.session.service.ServiceSessionDefinition;

public interface RemoteServiceManager {
    ServiceSession openServiceSession(ServiceSessionDefinition definition);
}
