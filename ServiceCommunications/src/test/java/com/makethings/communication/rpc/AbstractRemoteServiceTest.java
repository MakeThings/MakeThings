package com.makethings.communication.rpc;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
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
    private AbstractRemoteService service;

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

    private void givenExceptionWhenRequestCredentials() {
        Mockito.when(serviceCredentialsProvider).thenThrow(new RuntimeException("qwefqwfe"));
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
