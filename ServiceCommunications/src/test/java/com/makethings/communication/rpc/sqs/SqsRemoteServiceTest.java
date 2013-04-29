package com.makethings.communication.rpc.sqs;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.makethings.communication.amazon.AmazonServiceCredentials;
import com.makethings.communication.rpc.ServiceManager;
import com.makethings.communication.rpc.TestServiceManagerHelper;
import com.makethings.communication.rpc.json.JsonRpcHandler;
import com.makethings.communication.rpc.json.JsonRpcRequest;
import com.makethings.communication.session.service.DefaultServiceSession;
import com.makethings.communication.session.service.ServiceSessionDefinition;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/SqsRemoteServiceTest.xml")
public class SqsRemoteServiceTest {

    private static final ReceiveMessageResult EMPTY_MESSAGE = new ReceiveMessageResult();

    @Autowired
    private SqsQueue queue;

    @Autowired
    private AWSCredentials awsCredentials;

    @Autowired
    private SqsRemoteService remoteService;

    @Autowired
    private AmazonServiceCredentials queueServiceCredentials;

    @Autowired
    private ServiceManager serviceManager;

    private TestServiceManagerHelper testServiceManagerHelper;

    @Autowired
    private ServiceSessionDefinition sessionDefinition;

    @Autowired
    private DefaultServiceSession serviceSession;

    @Autowired
    private JsonRpcHandler jsonRpcHandler;

    @Before
    public void setUp() {
        testServiceManagerHelper = new TestServiceManagerHelper(serviceManager);
        testServiceManagerHelper.givenCreatedSessionByDefinition(serviceSession, sessionDefinition);

        givenQueueServiceCredentials();
    }

    @After
    public void tearDown() {
        remoteService.stop();
    }

    @Test
    @DirtiesContext
    public void givenServiceWhenInitialisedThenQueueServiceCreadentialsIsPopulatedToAQueue() {
        whenServiceInit();

        thenCreadentialsHasBeenPopulated();
    }

    @Test
    @DirtiesContext
    public void givenServiceWhenStartThenRequestToReceiveMessagesIsSent() {
        givenNoMessagesInAQueue();

        whenServiceStart();

        thenRequestToReceiveMessagesIsSent();
    }

    private void givenNoMessagesInAQueue() {
        Mockito.when(queue.receiveMessage(Matchers.isA(ReceiveMessageRequest.class))).thenReturn(EMPTY_MESSAGE);
    }

    @Test
    @DirtiesContext
    public void givenReceivedJsonRequestWhenProcesstingThenItIsHandled() {
        givenMessageInAQueue();

        whenServiceStart();

        thenMessageIsDispathedForProcessing();
    }

    @Test
    @DirtiesContext
    public void givenMultipleRequestsWhenProcessingThenAllOfThemAreHandled() {
        givenMessagesInAQueue();

        whenServiceStart();

        thenMessagesAreDispathedForProcessing();
    }

    private void thenMessagesAreDispathedForProcessing() {
        verify(jsonRpcHandler, timeout(5 * 1000)).handle(eq(new JsonRpcRequest().withMessages("One")));
        verify(jsonRpcHandler, timeout(5 * 1000)).handle(eq(new JsonRpcRequest().withMessages("Two")));
        verify(jsonRpcHandler, timeout(5 * 1000)).handle(eq(new JsonRpcRequest().withMessages("Three")));
    }

    private void givenMessagesInAQueue() {
        ReceiveMessageResult receiveMessageResult1 = new ReceiveMessageResult().withMessages(createMessage("One"), createMessage("Two"));
        ReceiveMessageResult receiveMessageResult2 = new ReceiveMessageResult().withMessages(createMessage("Three"));
        Mockito.when(queue.receiveMessage(Matchers.isA(ReceiveMessageRequest.class))).thenReturn(receiveMessageResult1)
                .thenReturn(receiveMessageResult2).thenReturn(EMPTY_MESSAGE);
    }

    private void thenMessageIsDispathedForProcessing() {
        JsonRpcRequest req = new JsonRpcRequest().withMessages("Json request...");
        Mockito.verify(jsonRpcHandler, Mockito.timeout(5 * 1000)).handle(Matchers.eq(req));
    }

    private void givenMessageInAQueue() {
        ReceiveMessageResult receiveMessageResult = new ReceiveMessageResult().withMessages(createMessage());
        Mockito.when(queue.receiveMessage(Matchers.isA(ReceiveMessageRequest.class))).thenReturn(receiveMessageResult)
                .thenReturn(EMPTY_MESSAGE);
    }

    private Message createMessage() {
        return createMessage("Json request...");
    }

    private Message createMessage(String body) {
        return new Message().withBody(body);
    }

    private void thenRequestToReceiveMessagesIsSent() {
        Mockito.verify(queue, timeout(5 * 1000).atLeast(1)).receiveMessage(Matchers.isA(ReceiveMessageRequest.class));
    }

    private void whenServiceStart() {
        remoteService.init();
        remoteService.start();
    }

    private void givenQueueServiceCredentials() {
        Mockito.when(queueServiceCredentials.getAwsCredentials()).thenReturn(awsCredentials);
    }

    private void whenServiceInit() {
        remoteService.init();
    }

    private void thenCreadentialsHasBeenPopulated() {
        Mockito.verify(queue).setAwsCredentials(awsCredentials);
    }
}
