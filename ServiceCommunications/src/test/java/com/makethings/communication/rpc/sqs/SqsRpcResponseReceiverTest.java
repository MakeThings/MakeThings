package com.makethings.communication.rpc.sqs;

import java.lang.reflect.Method;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.makethings.communication.rpc.json.JsonClientRequest;
import com.makethings.communication.rpc.json.JsonClientResponse;
import com.makethings.communication.rpc.json.TestIdeaService;
import com.makethings.communication.support.FileHelper;

@RunWith(MockitoJUnitRunner.class)
public class SqsRpcResponseReceiverTest {

    private static final String A_REQUEST_MESSAGE = null;
    private static final String A_REQUEST_ID = "message_reguest_id";
    private static final String A_RESPONSE_QUEUE_NAME = "req_queue";

    private SqsRpcResponseReceiver receiver;

    private static Method CREATE_NEW_IDEA_METHOD;

    private JsonClientRequest jsonClientRequest;
    private JsonClientResponse response;

    @Before
    public void setUp() {
        receiver = new SqsRpcResponseReceiver(A_RESPONSE_QUEUE_NAME, queue);
    }
    
    @Mock
    private SqsQueue queue;
    
    static {
        try {
            CREATE_NEW_IDEA_METHOD = TestIdeaService.class.getDeclaredMethod("createNewIdea", String.class);
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
        }

    }

    @Test
    public void receiverVerifiesThatReceivedMessageHasExpectedResponseId() {
        givenRequest();
        givenResponseMessages();
        
        whenReceiveResponse();
    
        thenReponseHasResponseId();
    }
    
    private void thenReponseHasResponseId() {
        Assert.assertThat(response.getReponseId(), Matchers.is(A_REQUEST_ID));
    }

    private void givenResponseMessages() {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(A_RESPONSE_QUEUE_NAME);
        ReceiveMessageResult receivedMessages = new ReceiveMessageResult().withMessages(messageWithExpectedResponseId(), messageWithOtherResponseId());
        Mockito.when(queue.receiveMessage(Mockito.eq(receiveMessageRequest))).thenReturn(receivedMessages);
    }

    private Message messageWithOtherResponseId() {
        String response = "{\"Res\":{\"jsonrpc\":\"2.0\",\"id\":\"" + A_REQUEST_ID + "\",\"result\":200}}";
        return new Message().withBody(response);
    }

    private Message messageWithExpectedResponseId() {
        String response = FileHelper.readFromFilename("/json/createIdeaServiceResponse.txt");
        return new Message().withBody(response);
    }


    private void whenReceiveResponse() {
        response = receiver.receiveResponseFor(jsonClientRequest);
    }

    private void givenRequest() {
        jsonClientRequest = new JsonClientRequest(CREATE_NEW_IDEA_METHOD, A_REQUEST_MESSAGE, A_REQUEST_ID);
    }
}
