package com.makethings.communication.rpc;

import static org.mockito.Mockito.when;

import java.rmi.Remote;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.makethings.communication.session.service.DefaultServiceSession;
import com.makethings.communication.session.service.ServiceSessionDefinition;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/AbstractRemoteServiceTest.xml")
public class AbstractRemoteServiceTest {

    @Autowired
    private TestRemoteService service;

    private TestRemoteServiceHelper testRemoteServiceHelper;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private ServiceManager serviceManager;

    @Autowired
    private ServiceSessionDefinition sessionDefinition;

    private DefaultServiceSession serviceSession;

    @Before
    public void setUp() {
        testRemoteServiceHelper = new TestRemoteServiceHelper(service, expectedException);
        givenServiceSession();
        givenServiceManager();
    }

    @Test
    @DirtiesContext
    public void givenServiceDefinitionWhenInitServiceThenSessionShouldBeCreated() {
        whenServiceInit();
        thenSessionIsCreated();
    }

    @Test
    @DirtiesContext
    public void givenInitedServiceWhenCallInitAgainThenExeptionShouldBeThrown() {
        testRemoteServiceHelper.expectRemoteServiceException();
        givenServiceInited();
        whenServiceInit();
    }

    @Test
    @DirtiesContext
    public void givenExceptionWhenInitThenRemoteExceptionShouldBeThrown() {
        testRemoteServiceHelper.expectRemoteServiceException();
        givenExceptionWhenOpenSession();

        givenServiceInited();
    }

    @Test
    @DirtiesContext
    public void givenServiceWhenStartThenProcessingShouldBeStated() {
        givenServiceInited();
        testRemoteServiceHelper.expextStartProcessing();

        whenServiceStart();

        testRemoteServiceHelper.thenProcessingIsStarted();
    }

    @Test
    @DirtiesContext
    public void givenStartedServiceWhenCallStartMethodAgainThenExceptionIsThrown() {
        givenServiceStarted();

        testRemoteServiceHelper.expectRemoteServiceException();

        whenServiceStart();
    }

    @Test
    @DirtiesContext
    public void givenRunningServiceWhenStopThenItShouldChangeStatusToStopped() {
        givenServiceStarted();

        whenStopService();

        thenServiceStatusIs(RemoteServiceState.STOPPED);
    }

    @Test
    @DirtiesContext
    public void givenRunningServiceWhenStopThenSessionShouldBeClosed() {
        givenServiceStarted();

        whenStopService();

        thenSessionIsClosed();
    }

    private void thenSessionIsClosed() {
        Mockito.verify(serviceManager).closeServiceSession(Matchers.eq(serviceSession.getId()));
    }

    private void thenServiceStatusIs(RemoteServiceState expectedStatus) {
        Assert.assertThat(service.getState(), CoreMatchers.is(expectedStatus));
    }

    private void whenStopService() {
        service.stop();
    }

    private void givenServiceStarted() {
        service.init();
        service.start();
    }

    private void whenServiceStart() {
        service.start();
    }

    private void givenExceptionWhenOpenSession() {
        when(serviceManager.openServiceSession(Matchers.same(sessionDefinition))).thenThrow(new RuntimeException("qwefqwfe"));
    }

    private void givenServiceInited() {
        service.init();
    }

    private void whenServiceInit() {
        service.init();
    }

    private void thenSessionIsCreated() {
        Mockito.verify(serviceManager).openServiceSession(Mockito.same(sessionDefinition));
    }

    private void givenServiceSession() {
        serviceSession = new DefaultServiceSession(sessionDefinition);
        serviceSession.setId("service session id");
    }

    private void givenServiceManager() {
        Mockito.when(serviceManager.openServiceSession(Mockito.same(sessionDefinition))).thenReturn(serviceSession);
    }
}
