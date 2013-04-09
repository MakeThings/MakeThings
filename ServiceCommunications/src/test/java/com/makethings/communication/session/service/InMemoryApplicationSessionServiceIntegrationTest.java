package com.makethings.communication.session.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.makethings.communication.amazon.AmazonServiceCredentials;
import com.makethings.communication.session.ApplicationSession;
import com.makethings.communication.session.InMemoryApplicationSessionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/InMemoryApplicationSessionServiceIntegrationTest.xml")
public class InMemoryApplicationSessionServiceIntegrationTest {

    @Autowired
    private ServiceSessionDefinition serviceSessionDefinition;

    @Autowired
    private InMemoryApplicationSessionService service;

    @Test
    public void givenServiceSessionDefinitionWhenCreateSessionThenNewSessionInstanceShouldBeCreated() {
        ApplicationSession session = whenCreateServiceSession();

        ServiceSession serviceSession = thenItIsServiceSession(session);
        thenCommonServiceSessionParamPopulated(serviceSession);
        AmazonServiceCredentials amazonServiceCredentials = thenSessionHasAmazonAwsCredentials(session);
        thenAmazonAwsCredentialsPopulated(amazonServiceCredentials);
    }

    private void thenAmazonAwsCredentialsPopulated(AmazonServiceCredentials amazonServiceCredentials) {
        assertThat(amazonServiceCredentials.getAwsCredentials(), notNullValue());
        assertThat(amazonServiceCredentials.getAwsCredentials().getAWSAccessKeyId(), notNullValue());
        assertThat(amazonServiceCredentials.getAwsCredentials().getAWSSecretKey(), notNullValue());
    }

    private AmazonServiceCredentials thenSessionHasAmazonAwsCredentials(ApplicationSession session) {
        assertThat(session.getQueueServiceCredentials(), instanceOf(AmazonServiceCredentials.class));
        AmazonServiceCredentials amazonServiceCredentials = (AmazonServiceCredentials) session.getQueueServiceCredentials();
        return amazonServiceCredentials;
    }

    private void thenCommonServiceSessionParamPopulated(ServiceSession serviceSession) {
        assertThat(serviceSession.getId(), notNullValue());
        assertThat(serviceSession.getRequstQueueName(), equalTo("ServiceReqQueue"));
        assertThat(serviceSession.getServiceName(), equalTo("TestService"));
    }

    private ServiceSession thenItIsServiceSession(ApplicationSession session) {
        assertThat(session, instanceOf(ServiceSession.class));
        ServiceSession serviceSession = (ServiceSession) session;
        return serviceSession;
    }

    private ApplicationSession whenCreateServiceSession() {
        ApplicationSession session = service.createNewSession(serviceSessionDefinition);
        return session;
    }
}
