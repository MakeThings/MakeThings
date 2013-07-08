package com.makethings.communication.rpc.sqs;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.makethings.communication.rpc.RemoteServiceClientException;
import com.makethings.communication.rpc.json.JsonClientRequest;
import com.makethings.communication.rpc.json.JsonClientResponse;
import com.makethings.communication.rpc.json.TestIdeaService;
import com.makethings.communication.support.FileHelper;

@RunWith(MockitoJUnitRunner.class)
public class SqsRpcResponseReceiverTest {

	private static final String A_REQUEST_MESSAGE = null;
	private static final String A_REQUEST_ID = "message_reguest_id";
	private static final String A_RESPONSE_QUEUE_NAME = "req_queue";
	private static final long A_RECEIVE_TIMEOUT = 1000;

	private SqsRpcResponseReceiver receiver;

	private static Method CREATE_NEW_IDEA_METHOD;

	private JsonClientRequest jsonClientRequest;
	private JsonClientResponse response;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setUp() {
		receiver = new SqsRpcResponseReceiver(A_RESPONSE_QUEUE_NAME, queue);
	}

	@Mock
	private SqsQueue queue;

	static {
		try {
			CREATE_NEW_IDEA_METHOD = TestIdeaService.class.getDeclaredMethod(
					"createNewIdea", String.class);
		} catch (Exception e) {
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

	@Test
	public void receivedReponseHasRpcJsonMessage() {
		givenRequest();
		givenResponseMessages();

		whenReceiveResponse();

		thenResponseHasAmessage();
	}

	@Test
	public void receivedReponseHasInvokedMethod() {
		givenRequest();
		givenResponseMessages();

		whenReceiveResponse();

		thenResponseHasAmethod();
	}

	@Test
	public void receiverWaitForReponseUntilTimemout() {
		givenRequest();
		givenDelay();

		expectExceptionThrown();

		whenReceiveResponse();
	}

	private void expectExceptionThrown() {
		expectedException.expect(RemoteServiceClientException.class);
	}

	private void givenDelay() {
		receiver.setReceiveTimeout(A_RECEIVE_TIMEOUT);
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(
				A_RESPONSE_QUEUE_NAME);
		when(queue.receiveMessage(Mockito.eq(receiveMessageRequest)))
				.thenAnswer(delayedReceiveMessageResult()).thenAnswer(
						delayedReceiveMessageResult());
	}

	private Answer<ReceiveMessageResult> delayedReceiveMessageResult() {
		return new Answer<ReceiveMessageResult>() {

			@Override
			public ReceiveMessageResult answer(InvocationOnMock invocation)
					throws Throwable {
				Thread.sleep(A_RECEIVE_TIMEOUT);
				return new ReceiveMessageResult();
			}
		};
	}

	@Test
	public void receiverProceedReadingMessagesUntilExptedIsReceived() {
		givenRequest();
		givenResponseMessagesBatch();

		whenReceiveResponse();

		thenReponseHasResponseId();
	}

	private void givenResponseMessagesBatch() {
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(
				A_RESPONSE_QUEUE_NAME);
		ReceiveMessageResult receivedMessages1 = new ReceiveMessageResult()
				.withMessages(messageWithOtherResponseId(),
						messageWithOtherResponseId());
		ReceiveMessageResult receivedMessages2 = new ReceiveMessageResult()
				.withMessages(messageWithExpectedResponseId(),
						messageWithOtherResponseId());
		when(queue.receiveMessage(Mockito.eq(receiveMessageRequest)))
				.thenReturn(receivedMessages1).thenReturn(receivedMessages2);
	}

	private void thenResponseHasAmethod() {
		assertThat(response.getInvokedMethod(), is(CREATE_NEW_IDEA_METHOD));
	}

	private void thenResponseHasAmessage() {
		assertThat(response.getJsonRpcResponse(),
				is("{\"jsonrpc\":\"2.0\",\"id\":\"" + A_REQUEST_ID
						+ "\",\"result\":200}"));
	}

	private void thenReponseHasResponseId() {
		assertThat(response.getReponseId(), is(A_REQUEST_ID));
	}

	private void givenResponseMessages() {
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(
				A_RESPONSE_QUEUE_NAME);
		ReceiveMessageResult receivedMessages = new ReceiveMessageResult()
				.withMessages(messageWithExpectedResponseId(),
						messageWithOtherResponseId());
		when(queue.receiveMessage(Mockito.eq(receiveMessageRequest)))
				.thenReturn(receivedMessages);
	}

	private Message messageWithOtherResponseId() {
		String response = FileHelper
				.readFromFilename("/json/createIdeaServiceResponse.txt");
		return new Message().withBody(response);
	}

	private Message messageWithExpectedResponseId() {
		String response = "{\"Res\":{\"jsonrpc\":\"2.0\",\"id\":\""
				+ A_REQUEST_ID + "\",\"result\":200}}";
		return new Message().withBody(response);
	}

	private void whenReceiveResponse() {
		response = receiver.receiveResponseFor(jsonClientRequest);
	}

	private void givenRequest() {
		jsonClientRequest = new JsonClientRequest(CREATE_NEW_IDEA_METHOD,
				A_REQUEST_MESSAGE, A_REQUEST_ID);
	}
}
