package com.makethings.communication.rpc;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.makethings.communication.session.SessionIdProvider;
import com.makethings.communication.session.service.ServiceSession;
import com.makethings.communication.testsupport.SessionIdProviderMockHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/AbstractRemoteServiceTest.xml")
public class AbstractRemoteServiceTest {
    
    @Autowired
    private RemoteService service;

    private SessionIdProviderMockHelper sessionIdProviderMockHelper;

    @Autowired
    private SessionIdProvider sessionIdProvider;
    
    @Before
    public void setUp() {
        sessionIdProviderMockHelper = new SessionIdProviderMockHelper(sessionIdProvider);
    }
    
    @Test
    public void givenServiceDefinitionWhenInitServiceThenSessionShouldBeCreated() {
        sessionIdProviderMockHelper.givenSessionIdProvider("1111-2222");
        whenServiceInit();
        thenSessionIsCreated();
    }

    private void whenServiceInit() {
        service.init();
    }

    private void thenSessionIsCreated() {
        ServiceSession session = service.getSession();
        assertThat(session, CoreMatchers.notNullValue());
        assertThat(session.getId(), CoreMatchers.is("1111-2222"));
        assertThat(session.getRequstQueueName(), CoreMatchers.is("ServiceReqQueue"));
        assertThat(session.getServiceName(), CoreMatchers.is("TestService"));
        
    }
}
