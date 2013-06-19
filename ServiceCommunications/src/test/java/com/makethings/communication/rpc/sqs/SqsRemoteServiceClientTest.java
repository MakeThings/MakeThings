package com.makethings.communication.rpc.sqs;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.makethings.communication.rpc.ClientManager;
import com.makethings.communication.rpc.json.JsonClientMarshaler;
import com.makethings.communication.rpc.json.JsonClientRequest;
import com.makethings.communication.rpc.json.TestIdeaService;
import com.makethings.communication.session.user.DefaultUserSessionDefinition;
import com.makethings.communication.session.user.UserSession;

@RunWith(MockitoJUnitRunner.class)
public class SqsRemoteServiceClientTest {

    private static final String REMOTE_SERVICE_NAME = "SomeRemoteService";
    private static final String REQUEST_QUEUE_NAME = "RemoteServiceQueue";
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

    @Mock
    private JsonClientMarshaler clientMarshaler;

    @Before
    public void setUp() throws SecurityException, NoSuchMethodException {
        serviceClient = new SqsRemoteServiceClient();
        serviceClient.setClientManaget(clientManager);
        serviceClient.setQueue(queue);
        serviceClient.setJsonClientMarshaler(clientMarshaler);
        givenClientManager();
        givenJsonMarchaler();
    }

    private void givenJsonMarchaler() throws SecurityException, NoSuchMethodException {
        Method serviceMethod = TestIdeaService.class.getDeclaredMethod("createNewIdea", String.class);
        when(clientMarshaler.marshalClientRequest(CLIENT_SESSION_ID, serviceMethod, "foo")).thenReturn(new JsonClientRequest("foo"));
    }

    private void givenClientManager() {
        when(clientManager.getServiceRequestQueueName(REMOTE_SERVICE_NAME)).thenReturn(REQUEST_QUEUE_NAME);
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

    @Test
    public void givenMethodNameAndArgsWhenInvokeThenRequestIsSent() throws SecurityException, NoSuchMethodException {
        givenInitedClient();

        whenInvoke();

        thenRequstIsSent();
    }

    private void thenRequstIsSent() {
        verify(queue).sendMessage(Mockito.eq(new SendMessageRequest().withQueueUrl(REQUEST_QUEUE_NAME).withMessageBody("foo")));
    }

    private void whenInvoke() throws SecurityException, NoSuchMethodException {
        serviceClient.invoke(TestIdeaService.class.getDeclaredMethod("createNewIdea", String.class), "foo");
    }

    private void givenInitedClient() {
        givenUserSessionDefinition();
        serviceClient.init();
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
