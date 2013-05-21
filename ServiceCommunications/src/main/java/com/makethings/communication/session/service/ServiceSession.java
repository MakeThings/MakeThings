package com.makethings.communication.session.service;

import com.makethings.communication.session.ApplicationSession;

public interface ServiceSession extends ApplicationSession {

    String getRequestQueueName();

    String getServiceName();

}
