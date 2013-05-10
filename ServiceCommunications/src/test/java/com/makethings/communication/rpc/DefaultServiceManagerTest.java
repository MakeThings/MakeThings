package com.makethings.communication.rpc;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.when;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.makethings.communication.session.ApplicationSessionService;
import com.makethings.communication.session.service.ServiceSession;
import com.makethings.communication.session.service.ServiceSessionDefinition;

@RunWith(MockitoJUnitRunner.class)
public class DefaultServiceManagerTest {

    private DefaultServiceManager serviceManager;

    @Mock
    private ApplicationSessionService applicationSessionService;

    @Mock
    private ServiceSessionDefinition serviceSessionDefinition;

    @Mock
    private ServiceSession expectedOpenedSession;

    private ServiceSession openedSession;

    @Before
    public void setUp() {
        serviceManager = new DefaultServiceManager();
        serviceManager.setSessionService(applicationSessionService);

        giveWeHaveSessionService();
    }

    private void giveWeHaveSessionService() {
        when(applicationSessionService.createNewSession(same(serviceSessionDefinition))).thenReturn(expectedOpenedSession);
    }

    @Test
    public void givenServiceSessionDefinitionWhenOpenSessionThenSessionShouldBeCreated() {
        givenSessionDefinition();

        whenOpenSession();

        thenServiceSessionIsCreated();
    }

    private void thenServiceSessionIsCreated() {
        Assert.assertThat(openedSession, CoreMatchers.sameInstance(expectedOpenedSession));
    }

    private void whenOpenSession() {
        openedSession = serviceManager.openServiceSession(serviceSessionDefinition);
    }

    private void givenSessionDefinition() {
        assertNotNull(serviceSessionDefinition);
    }
}
