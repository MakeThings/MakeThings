package com.makethings.communication.session.user;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.makethings.communication.queue.ClientResponseQueueName;
import com.makethings.communication.session.ApplicationSession;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUserSessionFactoryTest {

    private static final String CLIENT_RESPONSE_QUEUE_NAME = "ClinetResponseQueueName";
    @Mock
    private ClientResponseQueueName responseQueueName;
    private DefaultUserSessionDefinition definition;
    private ApplicationSession session;
    private DefaultUserSessionFactory userSesssionFactory;

    @Before
    public void setUp() {
        userSesssionFactory = new DefaultUserSessionFactory();
    }

    @Test
    public void givenClientResponseQueueNameWhenCreateSessionThenSessionIsPopulatedWithTheQueueName() {
        givenSessionDefinition();

        whenCreateUserSession();

        thenResponseQueueNameIsPopulatedToTheSession();
    }

    private void thenResponseQueueNameIsPopulatedToTheSession() {
        assertThat(session, CoreMatchers.instanceOf(UserSession.class));
        UserSession userSession = (UserSession) session;
        assertThat(userSession.getResponseQueueName(), CoreMatchers.is(CLIENT_RESPONSE_QUEUE_NAME));
    }

    private void whenCreateUserSession() {
        session = definition.getSessionFactory().createSession(definition);
    }

    private void givenSessionDefinition() {
        Mockito.when(responseQueueName.getClientResponseQueueName()).thenReturn(CLIENT_RESPONSE_QUEUE_NAME);
        definition = new DefaultUserSessionDefinition();
        definition.setClientResponseQueueName(responseQueueName);
        definition.setUserSesssionFactory(userSesssionFactory);
    }
}