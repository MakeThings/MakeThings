package com.makethings.communication.session;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class DefaultApplicationSessionFactoryTest {

    private ApplicationSessionFactory applicationSessionFactory;

    @Test
    public void givenServiceDefinitionWhenCreateSessionThenServiceSessionShouldBeCreated() {
        givenWeHaveSessionFactory();
        ServiceSessionDefinition sessionDef = givenServiceSessionDefinition();

        ApplicationSession session = whenCreateNewSession(sessionDef);

        thenServiceSessionIsCreated(session);
    }

    @Test
    public void givenRequestQueueInDefinitionWhenCreateSessionThenRequestQueueIsAvailableInTheSession() {
        givenWeHaveSessionFactory();
        ServiceSessionDefinition sessionDef = givenServiceSessionDefinition();

        ApplicationSession session = whenCreateNewSession(sessionDef);

        thenServiceSessionIsCreated(session);
        thenRequestQeueueIs(ServiceSession.class.cast(session), "ServiceReqQueue");
    }

    private void thenRequestQeueueIs(ServiceSession session, String queueName) {
        Assert.assertThat(session.getRequstQueueName(), CoreMatchers.equalTo(queueName));
    }

    private void thenServiceSessionIsCreated(ApplicationSession session) {
        Assert.assertThat(session, CoreMatchers.instanceOf(ServiceSession.class));
    }

    private ApplicationSession whenCreateNewSession(ServiceSessionDefinition sessionDef) {
        return applicationSessionFactory.createSession(sessionDef);
    }

    private ServiceSessionDefinition givenServiceSessionDefinition() {
        ServiceSessionDefinition def = new ServiceSessionDefinition();
        def.setRequestQueueName("ServiceReqQueue");
        return def;
    }

    private void givenWeHaveSessionFactory() {
        applicationSessionFactory = new DefaultApplicationSessionFactory();
    }

}
