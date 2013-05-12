package com.makethings.communication.rpc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.makethings.communication.session.ApplicationSessionService;
import com.makethings.communication.session.CreateSessionException;
import com.makethings.communication.session.SessionException;
import com.makethings.communication.session.service.DefaultServiceSession;
import com.makethings.communication.session.service.ServiceSession;
import com.makethings.communication.session.service.ServiceSessionDefinition;
import com.makethings.communication.session.user.UserSession;

@RunWith(MockitoJUnitRunner.class)
public class DefaultServiceManagerTest {

    private static final String SERVICE_SESSION_ID = "123";

    private static final String USER_SESSION_ID = "456";

    private static final String USER_RESPOSE_QUEUE_NAME = "usr_resp_q_name";

    private DefaultServiceManager serviceManager;

    @Mock
    private ApplicationSessionService applicationSessionService;

    @Mock
    private ServiceSessionDefinition serviceSessionDefinition;

    @Mock
    private ServiceSession expectedOpenedSession;

    @Mock
    private UserSession userSession;

    private ServiceSession openedSession;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private String responseQueueName;

    @Before
    public void setUp() {
        serviceManager = new DefaultServiceManager();
        serviceManager.setSessionService(applicationSessionService);
        reset(applicationSessionService);
    }

    @Test
    public void givenServiceSessionDefinitionWhenOpenSessionThenSessionShouldBeCreated() {
        givenSessionDefinition();

        whenOpenSession();

        thenServiceSessionIsCreated();
    }

    @Test
    public void givenWrongTypeOfSessionWhenCreateSessionThenExceptionIsThrown() {
        expectExceptionWhenWrongSessionIsCreated();

        givenSessionDefinition();

        whenWrongSessionTypeCreated();
    }

    @Test
    public void givenSessionIdWhenCloseSessionThenCorrespondedSessionShouldBeClosed() {
        givenSessionId();

        whenCloseSession();

        thenServiceSessionIsClosed();
    }

    @Test
    public void givenClientSessionIdWhenGetClientResponseQueueNameThenQueueNameFromUserSessionIsReturned() {
        givenClientSessionId();

        whenGetClientResponseQueue();

        thenResponseQueueNameIsReturned();
    }

    @Test
    public void givenGivenClientSessionIdAndWrongSessionWhenGetClientResponseQueueNameThenExceptionIsThrown() {
        expectExceptionIfWrongSessionWhenGetClientResponseQueueName();

        givenClientSessionId();

        whenGetClientResponseQueueAndWrongSessionIsReturned();
    }

    private void whenGetClientResponseQueueAndWrongSessionIsReturned() {
        ServiceSession wrongSession = Mockito.mock(ServiceSession.class);
        when(applicationSessionService.getSessionById(USER_SESSION_ID)).thenReturn(wrongSession);
        responseQueueName = serviceManager.getClientResponseQueueName(USER_SESSION_ID);
    }

    private void expectExceptionIfWrongSessionWhenGetClientResponseQueueName() {
        exception.expect(SessionException.class);
    }

    private void thenResponseQueueNameIsReturned() {
        assertThat(responseQueueName, equalTo(USER_RESPOSE_QUEUE_NAME));
    }

    private void whenGetClientResponseQueue() {
        when(applicationSessionService.getSessionById(USER_SESSION_ID)).thenReturn(userSession);
        responseQueueName = serviceManager.getClientResponseQueueName(USER_SESSION_ID);
    }

    private void givenClientSessionId() {
        userSession = Mockito.mock(UserSession.class);
        when(userSession.getId()).thenReturn(USER_SESSION_ID);
        when(userSession.getResponseQueueName()).thenReturn(USER_RESPOSE_QUEUE_NAME);
    }

    private void thenServiceSessionIsClosed() {
        verify(applicationSessionService).deleteSessionById(SERVICE_SESSION_ID);
    }

    private void whenCloseSession() {
        serviceManager.closeServiceSession(SERVICE_SESSION_ID);
    }

    private void givenSessionId() {
        DefaultServiceSession tmpSession = new DefaultServiceSession(serviceSessionDefinition);
        tmpSession.setId(SERVICE_SESSION_ID);
        openedSession = tmpSession;
    }

    private void expectExceptionWhenWrongSessionIsCreated() {
        exception.expect(CreateSessionException.class);
    }

    private void whenWrongSessionTypeCreated() {
        when(applicationSessionService.createNewSession(same(serviceSessionDefinition))).thenReturn(userSession);
        openedSession = serviceManager.openServiceSession(serviceSessionDefinition);
    }

    private void thenServiceSessionIsCreated() {
        Assert.assertThat(openedSession, CoreMatchers.sameInstance(expectedOpenedSession));
    }

    private void whenOpenSession() {
        when(applicationSessionService.createNewSession(same(serviceSessionDefinition))).thenReturn(expectedOpenedSession);
        openedSession = serviceManager.openServiceSession(serviceSessionDefinition);
    }

    private void givenSessionDefinition() {
        assertNotNull(serviceSessionDefinition);
    }
}
