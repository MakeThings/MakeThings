package com.makethings.communication.rpc;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
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

    @Autowired
    private DefaultServiceSession serviceSession;

    private TestServiceManagerHelper testServiceManagerHelper;

    @Before
    public void setUp() {
        testRemoteServiceHelper = new TestRemoteServiceHelper(service, expectedException);
        testServiceManagerHelper = new TestServiceManagerHelper(serviceManager);
        testServiceManagerHelper.givenCreatedSessionByDefinition(serviceSession, sessionDefinition);
    }

    @Test
    @DirtiesContext
    public void givenServiceDefinitionWhenInitServiceThenSessionShouldBeCreated() {
        whenServiceInit();
        testServiceManagerHelper.thenSessionIsCreatedByDefinition(sessionDefinition);
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
        testServiceManagerHelper.givenExceptionWhenOpenSession();

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

        testServiceManagerHelper.thenSessionIsClosed(serviceSession);
    }
    
    @Test
    @DirtiesContext
    public void givenCreatedServiceWhenStopThenItShouldChangeStatusToStopped() {
        whenStopService();
        
        thenServiceStatusIs(RemoteServiceState.STOPPED);
    }
    
    @Test
    @DirtiesContext
    public void givenExceptionWhileRunningThenServiceChangeStateToError() {
        testRemoteServiceHelper.givenErrorWhenProcessing();
        givenServiceInited();
        
        whenServiceStart();

        delay();
        thenServiceStatusIs(RemoteServiceState.ERROR);
    }

    @Test
    @DirtiesContext
    public void givenRunningServiceThenServiceSessionIsUpdated() {
        givenServiceStarted();
        
        testServiceManagerHelper.thenServiceStartIsReported();
    }

    private void delay() {
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
        }
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
    
    private void givenServiceInited() {
        service.init();
    }

    private void whenServiceInit() {
        service.init();
    }

}
