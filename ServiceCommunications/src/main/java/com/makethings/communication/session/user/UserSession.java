package com.makethings.communication.session.user;

import com.makethings.communication.session.ApplicationSession;

public interface UserSession extends ApplicationSession {
    String getResponseQueueName();
}
