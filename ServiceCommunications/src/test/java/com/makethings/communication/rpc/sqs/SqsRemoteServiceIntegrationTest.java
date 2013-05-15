package com.makethings.communication.rpc.sqs;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.makethings.communication.rpc.RemoteServiceState;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring/SqsRemoteServiceIntegrationTest.xml" })
public class SqsRemoteServiceIntegrationTest {

    @Autowired
    private SqsRemoteService remoteService;

    @Test
    @DirtiesContext
    public void givenRemoteServiceConfiguredViaSpringThenInstanceOfTheServiceShouldBeAutowired() {
        Assert.assertNotNull(remoteService);

    }

    @Test
    @DirtiesContext
    public void givenRemoteServiceWhenInitThenStatusShouldBeWaitingToStart() {
        remoteService.init();
        Assert.assertThat(remoteService.getState(), CoreMatchers.is(RemoteServiceState.WAITING_TO_STAT));
    }
    
    @Test
    @DirtiesContext
    public void givenRemoteServiceWhenStartThenStatusShouldBeRunning() {
        remoteService.init();
        remoteService.start();
        delay(5000);
        Assert.assertThat(remoteService.getState(), CoreMatchers.is(RemoteServiceState.RUNNING));
    }
    
    private void delay(int delay) {
        try {
            Thread.sleep(delay);
        }
        catch (InterruptedException e) {
        }
    }

    @After
    public void tearDown() {
        if (remoteService.getState() == RemoteServiceState.RUNNING) {
            remoteService.stop();
        }
    }
    
}
