package com.makethings.communication.rpc;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.makethings.communication.session.ApplicationSessionService;
import com.makethings.communication.session.CreateSessionException;
import com.makethings.communication.session.service.ServiceSession;
import com.makethings.communication.session.service.ServiceSessionDefinition;
import com.makethings.communication.session.user.UserSession;

@RunWith(MockitoJUnitRunner.class)
public class DefaultServiceManagerTest {

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
