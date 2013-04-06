package com.makethings.communication.session;

import java.util.UUID;

public class UUIDSessionIdProvider implements SessionIdProvider {

    public String getSessionId() {
        return UUID.randomUUID().toString();
    }

}
