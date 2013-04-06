package com.makethings.communication.session.service;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.makethings.communication.session.ApplicationSession;
import com.makethings.communication.session.InMemoryApplicationSessionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/test-service-session.ctx.xml")
public class InMemoryApplicationSessionServiceTest {

    @Autowired
    private InMemoryApplicationSessionService service;

    @Autowired
    private ServiceSessionDefinition serviceSessionDefinition;

    @Test
    public void givenSessionDefinitionWhenCreateSessionThenSessionInstanceIsCreated() {
        givenWeHaveSessionDefinition();

        ApplicationSession session = whenCreateNewSession();

        ServiceSession serviceSession = thenItIsServiceSession(session);
    }

    private ServiceSession thenItIsServiceSession(ApplicationSession session) {
        Assert.assertThat(session, CoreMatchers.instanceOf(ServiceSession.class));
        return ServiceSession.class.cast(session);
    }

    private ApplicationSession whenCreateNewSession() {
        return service.createNewSession(serviceSessionDefinition);
    }

    private void givenWeHaveSessionDefinition() {
    }
}
