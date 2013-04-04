package com.makethings.communication.session.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.makethings.communication.queue.QueueServiceCredentials;
import com.makethings.communication.queue.QueueServiceCredentialsProvider;
import com.makethings.communication.session.ApplicationSession;
import com.makethings.communication.session.SessionIdProvider;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/test-service-session.ctx.xml")
public class DefaultServiceSessionFactoryTest {

    private static final String SERVICE_REQ_QUEUE = "ServiceReqQueue";

    @Autowired(required = true)
    private ServiceSessionDefinition serviceSessionDefinition;

    @Autowired
    private SessionIdProvider mockSessionIdProvider;

    @Autowired
    private QueueServiceCredentialsProvider<QueueServiceCredentials> credentialsProvider;
  
    @Mock
    private QueueServiceCredentials credentials;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenServiceDefinitionWhenCreateSessionThenServiceSessionShouldBeCreated() {
        givenServiceSessionDefinition();

        ApplicationSession session = whenCreateNewSession(serviceSessionDefinition);

        thenServiceSessionIsCreated(session);
    }

    @Test
    public void givenRequestQueueNameInDefinitionTheNameIsAvailableInTheSession() {
        givenServiceSessionDefinition();

        ApplicationSession session = whenCreateNewSession(serviceSessionDefinition);

        ServiceSession serviceSession = thenServiceSessionIsCreated(session);
        thenRequestQueueNameIs(serviceSession, SERVICE_REQ_QUEUE);
    }

    @Test
    public void givenServiceDefinitionWhenCreateSessionThenSessionIdShouldBeGenerated() {
        givenSessionIdProvider("1111-2222");
        givenServiceSessionDefinition();

        ApplicationSession session = whenCreateNewSession(serviceSessionDefinition);

        ServiceSession serviceSession = thenServiceSessionIsCreated(session);
        thenSessionIdIs(serviceSession, "1111-2222");
    }
    
    @Test
    public void givenSessionDefinitionThenQueueServiceCredentialsIsPopulated() {
        givenServiceSessionDefinition();
        givenCredentialsProvider();
        
        ApplicationSession session = whenCreateNewSession(serviceSessionDefinition);

        ServiceSession serviceSession = thenServiceSessionIsCreated(session);
        thenServiceNameIs(serviceSession, SERVICE_REQ_QUEUE);
        thenCredentialsIsPopilated(serviceSession); 
    }
    
    private void givenCredentialsProvider() {
        when(credentialsProvider.getCredentials()).thenReturn(credentials);
    }

    private void thenCredentialsIsPopilated(ApplicationSession session) {
        QueueServiceCredentials cred = session.getQueueServiceCredentials();
        assertThat(cred, is(credentials));
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    private void thenServiceNameIs(ServiceSession serviceSession, String serviceReqQueue) {
        assertThat(serviceSession.getServiceName(), is("TestService"));
    }

    private void thenSessionIdIs(ApplicationSession session, String expectedId) {
        assertThat(session.getId(), is(expectedId));
    }

    private void givenSessionIdProvider(String expectedSessionId) {
        when(mockSessionIdProvider.getSessionId()).thenReturn(expectedSessionId);
    }

    private void thenRequestQueueNameIs(ServiceSession session, String queueName) {
        assertThat(session.getRequstQueueName(), equalTo(queueName));
    }

    private void givenServiceSessionDefinition() {
        //serviceSessionDefinition.setRequestQueueName(SERVICE_REQ_QUEUE);
    }

    private ApplicationSession whenCreateNewSession(ServiceSessionDefinition sessionDef) {
        return sessionDef.getSessionFactory().createSession(sessionDef);
    }

    private ServiceSession thenServiceSessionIsCreated(ApplicationSession session) {
        Assert.assertThat(session, CoreMatchers.instanceOf(ServiceSession.class));
        return (ServiceSession) session;
    }
}
