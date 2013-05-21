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
import com.makethings.communication.session.SessionIdProvider;
import com.makethings.communication.testsupport.SessionIdProviderMockHelper;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUserSessionFactoryTest {

    private static final String CLIENT_RESPONSE_QUEUE_NAME = "ClinetResponseQueueName";
    private static final String USER_SESSION_ID = "2";
    @Mock
    private ClientResponseQueueName responseQueueName;
    private DefaultUserSessionDefinition definition;
    private ApplicationSession session;
    private DefaultUserSessionFactory userSesssionFactory;
    private SessionIdProviderMockHelper sessionIdProviderMockHelper;
    @Mock
    private SessionIdProvider idProvider;

    @Before
    public void setUp() {
        sessionIdProviderMockHelper = new SessionIdProviderMockHelper(idProvider);
        userSesssionFactory = new DefaultUserSessionFactory();
        userSesssionFactory.setSessionIdProvider(idProvider);
    }

    @Test
    public void givenClientResponseQueueNameWhenCreateSessionThenSessionIsPopulatedWithTheQueueName() {
        givenSessionDefinition();

        whenCreateUserSession();

        thenResponseQueueNameIsPopulatedToTheSession();
    }

    @Test
    public void givenClientTypeWhenCreateSessionThenClientTypeIsPopulatedToTheSession() {
        givenSessionDefinition();

        whenCreateUserSession();

        thenClientTypeIsPopulatedToSession();
    }

    @Test
    public void givenUserSessionDefWhenCreateSessionThenSessionIdGenerated() {
        givenSessionDefinition();

        whenCreateUserSession();

        thenSessionIdIsPopulated();
    }

    private void thenSessionIdIsPopulated() {
        assertThat(userSession().getId(), CoreMatchers.is(USER_SESSION_ID));
    }

    private void thenClientTypeIsPopulatedToSession() {
        assertThat(userSession().getClientType(), CoreMatchers.is(ClientType.SERVICE));

    }

    private void thenResponseQueueNameIsPopulatedToTheSession() {
        assertThat(userSession().getResponseQueueName(), CoreMatchers.is(CLIENT_RESPONSE_QUEUE_NAME));
    }

    private UserSession userSession() {
        assertThat(session, CoreMatchers.instanceOf(UserSession.class));
        UserSession userSession = (UserSession) session;
        return userSession;
    }

    private void whenCreateUserSession() {
        session = definition.getSessionFactory().createSession(definition);
    }

    private void givenSessionDefinition() {
        sessionIdProviderMockHelper.givenSessionIdProvider(USER_SESSION_ID);
        Mockito.when(responseQueueName.getClientResponseQueueName()).thenReturn(CLIENT_RESPONSE_QUEUE_NAME);
        definition = new DefaultUserSessionDefinition();
        definition.setClientResponseQueueName(responseQueueName);
        definition.setUserSesssionFactory(userSesssionFactory);
        definition.setClientType(ClientType.SERVICE);
    }
}
