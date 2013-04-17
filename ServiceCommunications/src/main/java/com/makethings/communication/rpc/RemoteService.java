package com.makethings.communication.rpc;

import com.makethings.communication.session.service.ServiceSession;

public interface RemoteService {
    ServiceSession getSession();
    void init();
}
