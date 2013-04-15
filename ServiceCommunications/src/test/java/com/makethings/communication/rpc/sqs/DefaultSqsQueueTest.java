package com.makethings.communication.rpc.sqs;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.makethings.communication.rpc.QueueException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/DefaultSqsQueueTest.xml")
public class DefaultSqsQueueTest {

    private static final String REQUEST = "Request";
    private static final String QUEUE_NAME = "ideaServiceRequestQueue";

    @Autowired
    private SqsQueue sqsQueue;

    @Autowired
    private AWSCredentials credentials;

    @Autowired
    private AmazonSQS sqsClient;
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private SendMessageRequest messageRequest;
    private SendMessageResult sendResult;
    private SendMessageResult expectedSendResult;
    private ReceiveMessageRequest receiveMessageRequest;
    private ReceiveMessageResult receiveMessageResult;
    private ReceiveMessageResult expectedReceiveMessageResult;

    @Test
    public void givenMessageRequestWhenSendThenResultShouldBeReturned() {
        givenQueue();
        givenMessageRequest();

        whenSendMessageRequest();

        thenSendResultWasCreated();
    }

    @Test
    public void givenMessageRequestWhenSendThenItShouldBePassedToSqsClient() {
        givenQueue();
        givenMessageRequest();
        givenSqsClient();

        whenSendMessageRequest();

        thenSqsClientWasCalled();
    }

    @Test
    public void givenReceiveMessageRequestWhenReceiveThenResultShouldBeReturned() {
        givenQueue();
        givenReceiveMessageRequest();
        givenSqsClient();

        whenReceive();

        thenReceiveResultWasReceived();
    }
    
    @Test
    public void givenAmazonExeptionWhenReceiveThenQueueExeptionShouldBeThrown() {
        givenQueue();
        givenReceiveMessageRequest();
        givenSqsClientWhenExceptionOccurs();
        expectQueueException();
        
        whenReceive();

    }

    private void expectQueueException() {
        expectedException.expect(QueueException.class);
    }

    private void givenSqsClientWhenExceptionOccurs() {
        Mockito.when(sqsClient.receiveMessage(receiveMessageRequest)).thenThrow(new AmazonServiceException("Some Error"));
    }

    private void thenReceiveResultWasReceived() {
        assertThat(receiveMessageResult, notNullValue());
        assertThat(receiveMessageResult, is(expectedReceiveMessageResult));
    }

    private void whenReceive() {
        receiveMessageResult = sqsQueue.receiveMessage(receiveMessageRequest);
    }

    private void givenReceiveMessageRequest() {
        receiveMessageRequest = new ReceiveMessageRequest().withQueueUrl(QUEUE_NAME);
    }

    private void thenSqsClientWasCalled() {
        Mockito.verify(sqsClient).sendMessage(Matchers.same(messageRequest));
    }

    private void givenSqsClient() {
        expectedSendResult = new SendMessageResult();
        Mockito.when(sqsClient.sendMessage(messageRequest)).thenReturn(expectedSendResult);
        expectedReceiveMessageResult = new ReceiveMessageResult();
        Mockito.when(sqsClient.receiveMessage(receiveMessageRequest)).thenReturn(expectedReceiveMessageResult);
    }

    private void thenSendResultWasCreated() {
        Assert.assertThat(sendResult, CoreMatchers.notNullValue());
    }

    private void whenSendMessageRequest() {
        sendResult = sqsQueue.sendMessage(messageRequest);
    }

    private void givenMessageRequest() {
        messageRequest = new SendMessageRequest().withQueueUrl(QUEUE_NAME).withMessageBody(REQUEST);
    }

    private void givenQueue() {
        sqsQueue.setAwsCredentials(credentials);
    }

}
