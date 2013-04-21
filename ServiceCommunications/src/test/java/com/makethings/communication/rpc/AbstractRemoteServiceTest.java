package com.makethings.communication.rpc;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.makethings.communication.queue.QueueServiceCredentials;
import com.makethings.communication.queue.QueueServiceCredentialsProvider;
import com.makethings.communication.session.SessionIdProvider;
import com.makethings.communication.session.service.ServiceSession;
import com.makethings.communication.testsupport.SessionIdProviderMockHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/AbstractRemoteServiceTest.xml")
public class AbstractRemoteServiceTest {
    
    @Autowired
    private TestRemoteService service;

    private TestRemoteServiceHelper testRemoteServiceHelper;

    private SessionIdProviderMockHelper sessionIdProviderMockHelper;

    @Autowired
    private SessionIdProvider sessionIdProvider;
    
    @Autowired
    private QueueServiceCredentialsProvider<QueueServiceCredentials> serviceCredentialsProvider ; 
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Before
    public void setUp() {
        sessionIdProviderMockHelper = new SessionIdProviderMockHelper(sessionIdProvider);
        testRemoteServiceHelper = new TestRemoteServiceHelper(service);
    }
    
    @Test
    @DirtiesContext
    public void givenServiceDefinitionWhenInitServiceThenSessionShouldBeCreated() {
        sessionIdProviderMockHelper.givenSessionIdProvider("1111-2222");
        whenServiceInit();
        thenSessionIsCreated();
    }
    
    @Test
    @DirtiesContext
    public void givenInitedServiceWhenCallInitAgainThenExeptionShouldBeThrown() {
        expectedException.expect(RemoteServiceException.class);
        sessionIdProviderMockHelper.givenSessionIdProvider("1111-2222");
        givenServiceInited();
        whenServiceInit();
    }
    
    @Test
    @DirtiesContext
    public void givenExceptionWhenInitThenRemoteExceptionShouldBeThrown() {
        expectedException.expect(RemoteServiceException.class);  
        sessionIdProviderMockHelper.givenSessionIdProvider("1111-2222");
        givenExceptionWhenRequestCredentials();
        
        givenServiceInited();
    }
    
    @Test
    @DirtiesContext
    public void givenServiceWhenStartThenProcessingShouldBeStated() {
        sessionIdProviderMockHelper.givenSessionIdProvider("1111-2222");
        givenServiceInited();
        testRemoteServiceHelper.expextStartProcessing();
        
        whenServiceStart();
        
        testRemoteServiceHelper.thenProcessingIsStarted();
    }

    private void whenServiceStart() {
        service.start();
    }

    private void givenExceptionWhenRequestCredentials() {
        Mockito.when(serviceCredentialsProvider.getCredentials()).thenThrow(new RuntimeException("qwefqwfe"));
    }

    private void givenServiceInited() {
        service.init();
    }

    private void whenServiceInit() {
        service.init();
    }

    private void thenSessionIsCreated() {
        ServiceSession session = service.getSession();
        assertThat(session, CoreMatchers.notNullValue());
        assertThat(session.getId(), CoreMatchers.is("1111-2222"));
        assertThat(session.getRequstQueueName(), CoreMatchers.is("ServiceReqQueue"));
        assertThat(session.getServiceName(), CoreMatchers.is("TestService"));
    }
}
