package com.makethings.communication.rpc.sqs;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.OutputStreamWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.makethings.communication.amazon.AmazonServiceCredentials;
import com.makethings.communication.rpc.ServiceManager;
import com.makethings.communication.rpc.TestServiceManagerHelper;
import com.makethings.communication.rpc.json.JsonRpcHandler;
import com.makethings.communication.rpc.json.JsonServiceRequest;
import com.makethings.communication.rpc.json.JsonServiceResponse;
import com.makethings.communication.session.service.DefaultServiceSession;
import com.makethings.communication.session.service.ServiceSessionDefinition;
import com.makethings.communication.support.FileHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/SqsRemoteServiceTest.xml")
public class SqsRemoteServiceTest {

    private static final String JSON_SERVICE_REQUEST_FILENAME = "/json/createIdeaServiceRequest.txt";
    private static final String JSON_SERVICE_REQUEST_ONE_FILENAME = "/json/createIdeaServiceRequestOne.txt";
    private static final String JSON_SERVICE_REQUEST_TWO_FILENAME = "/json/createIdeaServiceRequestTwo.txt";
    private static final String JSON_SERVICE_REQUEST_THREE_FILENAME = "/json/createIdeaServiceRequestThree.txt";
    private static final String JSON_SERVICE_RESPONSE_FILENAME = "/json/createIdeaServiceResponse.txt";
    private static final String JSON_RPC_RESPONSE_FILENAME = "/json/createIdeaJsonResponse.txt";

    private static final ReceiveMessageResult EMPTY_MESSAGE = new ReceiveMessageResult();
    private static final String CLIENT_SESSION_ID = "200";
    private static final String QUEUE_NAME = "ServiceReqQueue";

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

    private Message lastMessage;

    @Before
    public void setUp() {
        testServiceManagerHelper = new TestServiceManagerHelper(serviceManager);
        testServiceManagerHelper.givenCreatedSessionByDefinition(serviceSession, sessionDefinition);

        givenQueueServiceCredentials();
        givenJsonRpcHandler();
    }

    private void givenJsonRpcHandler() {
        Mockito.doAnswer(new Answer<Void>() {

            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                JsonServiceResponse response = (JsonServiceResponse) invocation.getArguments()[1];
                OutputStreamWriter ow = new OutputStreamWriter(response.getOutputStream());
                ow.write(FileHelper.readFromFilename(JSON_RPC_RESPONSE_FILENAME));
                ow.close();
                return null;
            }
        }).when(jsonRpcHandler).handle(Matchers.isA(JsonServiceRequest.class), Matchers.isA(JsonServiceResponse.class));
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
    public void givenReceivedJsonRequestWhenProcesstingThenItIsHandled() throws IOException {
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

    @Test
    @DirtiesContext
    public void givenProcessedRequestWhenHandlingThenMessageIsDeletedFromQueue() throws IOException {
        givenMessageInAQueue();

        whenServiceStart();

        thenMessageIsDeletedFromQueue();
    }

    @Test
    @DirtiesContext
    public void givenProcessedRequestWhenHandlingThenResponseIsSent() throws IOException {
        givenMessageInAQueue();
        givenClientResponseQueueName();

        whenServiceStart();

        thenResponseIsSent();
    }

    @Test
    @DirtiesContext
    public void givenCreateQueueOnStartupIsSetWhenIninThenQueueShouldBeCreatedIfNotExist() {
        givenRemoteServiceWithCreateQueueOnStartupIsSet();

        whenServiceInit();

        thenRequestToCreateQueueIsSent();
    }

    private void thenRequestToCreateQueueIsSent() {
        CreateQueueRequest expectedRequest = new CreateQueueRequest(QUEUE_NAME);
        verify(queue).createQueue(expectedRequest);
    }

    private void givenRemoteServiceWithCreateQueueOnStartupIsSet() {
        remoteService.setCreateQueueOnStartup(true);
    }

    private void givenClientResponseQueueName() {
        Mockito.when(serviceManager.getClientResponseQueueName(eq(CLIENT_SESSION_ID))).thenReturn("client_response_queue");
    }

    private void thenResponseIsSent() {
        SendMessageRequest sendRequest = new SendMessageRequest().withMessageBody(createServiceResponse()).withQueueUrl(
                "client_response_queue");
        verify(queue, timeout(2000)).sendMessage(eq(sendRequest));
    }

    private String createServiceResponse() {
        return FileHelper.readFromFilename(JSON_SERVICE_RESPONSE_FILENAME);
    }

    private void thenMessageIsDeletedFromQueue() {
        DeleteMessageRequest req = new DeleteMessageRequest().withQueueUrl(serviceSession.getRequestQueueName()).withReceiptHandle(
                lastMessage.getReceiptHandle());
        verify(queue, timeout(1000)).deleteMessage(eq(req));
    }

    private void thenMessagesAreDispathedForProcessing() {
        verifyThatHandlerExecutedWithRequest(FileHelper.readFromFilename(JSON_SERVICE_REQUEST_ONE_FILENAME));
        verifyThatHandlerExecutedWithRequest(FileHelper.readFromFilename(JSON_SERVICE_REQUEST_TWO_FILENAME));
        verifyThatHandlerExecutedWithRequest(FileHelper.readFromFilename(JSON_SERVICE_REQUEST_THREE_FILENAME));
    }

    private void givenMessagesInAQueue() {
        ReceiveMessageResult receiveMessageResult1 = new ReceiveMessageResult().withMessages(
                createMessageFromFile(JSON_SERVICE_REQUEST_ONE_FILENAME), createMessageFromFile(JSON_SERVICE_REQUEST_TWO_FILENAME));
        ReceiveMessageResult receiveMessageResult2 = new ReceiveMessageResult()
                .withMessages(createMessageFromFile(JSON_SERVICE_REQUEST_THREE_FILENAME));

        OngoingStubbing<ReceiveMessageResult> stubbing = when(queue.receiveMessage(isA(ReceiveMessageRequest.class)));
        stubbing.thenReturn(receiveMessageResult1).thenReturn(receiveMessageResult2).thenReturn(EMPTY_MESSAGE);
    }

    private void verifyThatHandlerExecutedWithRequest(String message) {
        verify(jsonRpcHandler, timeout(5 * 1000)).handle(eq(new JsonServiceRequest().withMessages(message)),
                Matchers.isA(JsonServiceResponse.class));
    }

    private void thenMessageIsDispathedForProcessing() {
        String message = FileHelper.readFromFilename(JSON_SERVICE_REQUEST_FILENAME);
        verifyThatHandlerExecutedWithRequest(message);
    }

    private void givenMessageInAQueue() {
        ReceiveMessageResult receiveMessageResult = new ReceiveMessageResult().withMessages(createMessage());
        Mockito.when(queue.receiveMessage(Matchers.isA(ReceiveMessageRequest.class))).thenReturn(receiveMessageResult)
                .thenReturn(EMPTY_MESSAGE);
    }

    private Message createMessage() {
        return createMessageFromFile(JSON_SERVICE_REQUEST_FILENAME);
    }

    private Message createMessage(String body) {
        lastMessage = new Message().withBody(body).withReceiptHandle(body + " rh");
        return lastMessage;
    }

    private Message createMessageFromFile(String fileName) {
        String body = FileHelper.readFromFilename(fileName);
        return createMessage(body);
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
