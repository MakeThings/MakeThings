package com.makethings.communication.rpc.sqs;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.makethings.communication.rpc.ClientManager;
import com.makethings.communication.session.user.DefaultUserSessionDefinition;
import com.makethings.communication.session.user.UserSession;

@RunWith(MockitoJUnitRunner.class)
public class SqsRemoteServiceClientTest {

    private static final String REMOTE_SERVICE_NAME = "SomeRemoteService";
    private static final Object REQUEST_QUEUE_NAME = null;
    private static final String CLIENT_REQUEST_QUEUE_NAME = "ClientRequestQueueName";
    private static final String CLIENT_SESSION_ID = "ClientSessionId";
    private DefaultUserSessionDefinition sessionDefinition;
    private SqsRemoteServiceClient serviceClient;

    @Mock
    private UserSession userSession;

    @Mock
    private ClientManager clientManager;
    
    @Mock
    private SqsQueue queue;

    @Before
    public void setUp() {
        serviceClient = new SqsRemoteServiceClient();
        serviceClient.setClientManaget(clientManager);
        serviceClient.setQueue(queue);
        
    }

    @Test
    public void givenUserSessionDefinitionWhenInitThenSessionIsCreated() {
        givenUserSessionDefinition();

        whenInit();

        thenSessionIsCreated();
    }

    @Test
    public void givenRemoteServiceNameWhenInitThenRequestQueueNameIsDerived() {
        givenUserSessionDefinition();

        whenInit();

        thenRequestQueueNameIsDerived();
    }
    
    @Test
    public void givenClientRequestQueueNameWhenInitThenQueueIsCreated() {
        givenUserSessionDefinition();

        whenInit();
        
        thenClientRequestQueueNameIsCreated();
    }

    private void thenClientRequestQueueNameIsCreated() {
        verify(queue).createQueue(new CreateQueueRequest().withQueueName(CLIENT_REQUEST_QUEUE_NAME));
    }

    private void thenRequestQueueNameIsDerived() {
        verify(clientManager).getServiceRequestQueueName(Mockito.eq(REMOTE_SERVICE_NAME));
        assertThat(serviceClient.getRequestQueueName(), CoreMatchers.equalTo(REQUEST_QUEUE_NAME));
    }

    private void givenRemoteServiceName() {
        serviceClient.setRemoteServiceName(REMOTE_SERVICE_NAME);
    }

    private void thenSessionIsCreated() {
        verify(clientManager).openClientSession(Mockito.same(sessionDefinition));
        assertThat(serviceClient.getUserSession(), sameInstance(userSession));
    }

    private void whenInit() {
        serviceClient.init();
    }

    private void givenUserSessionDefinition() {
        sessionDefinition = new DefaultUserSessionDefinition();
        serviceClient.setUserSessionDefinition(sessionDefinition);
        
        givenUserSession();
        givenRemoteServiceName();

    }

    private void givenUserSession() {
        when(clientManager.openClientSession(Mockito.same(sessionDefinition))).thenReturn(userSession);
        when(userSession.getResponseQueueName()).thenReturn(CLIENT_REQUEST_QUEUE_NAME);
        when(userSession.getId()).thenReturn(CLIENT_SESSION_ID);
    }
}
