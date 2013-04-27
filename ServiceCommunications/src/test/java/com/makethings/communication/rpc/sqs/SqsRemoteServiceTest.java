package com.makethings.communication.rpc.sqs;

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
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.makethings.communication.amazon.AmazonServiceCredentials;
import com.makethings.communication.rpc.ServiceManager;
import com.makethings.communication.rpc.TestServiceManagerHelper;
import com.makethings.communication.session.service.DefaultServiceSession;
import com.makethings.communication.session.service.ServiceSessionDefinition;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/SqsRemoteServiceTest.xml")
public class SqsRemoteServiceTest {

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

    @Before
    public void setUp() {
        testServiceManagerHelper = new TestServiceManagerHelper(serviceManager);
        testServiceManagerHelper.givenCreatedSessionByDefinition(serviceSession, sessionDefinition);

        givenQueueServiceCredentials();

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
        whenServiceStart();

        thenRequestToReceiveMessagesIsSent();
    }

    private void thenRequestToReceiveMessagesIsSent() {
        Mockito.verify(queue, Mockito.timeout(10 * 1000)).receiveMessage(Matchers.isA(ReceiveMessageRequest.class));
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
