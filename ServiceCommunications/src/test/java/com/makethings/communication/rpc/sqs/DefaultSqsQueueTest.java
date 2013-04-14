package com.makethings.communication.rpc.sqs;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

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

    private SendMessageRequest messageRequest;
    private SendMessageResult sendResult;
    private SendMessageResult expectedSendResult;

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

    private void thenSqsClientWasCalled() {
        Mockito.verify(sqsClient).sendMessage(Matchers.same(messageRequest));
    }

    private void givenSqsClient() {
        expectedSendResult = new SendMessageResult();
        Mockito.when(sqsClient.sendMessage(messageRequest)).thenReturn(expectedSendResult);
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
