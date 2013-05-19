package com.makethings.communication.session.user;

import com.makethings.communication.queue.ClientResponseQueueName;
import com.makethings.communication.session.ApplicationSessionDefinition;

public interface UserSessionDefinition extends ApplicationSessionDefinition {
    ClientResponseQueueName getClientResponseQueueName();
}
