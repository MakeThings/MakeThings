package com.makethings.communication.session.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.makethings.communication.session.ApplicationSession;
import com.makethings.communication.session.ApplicationSessionService;
import com.makethings.communication.session.SessionIdProvider;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/test-application-session.ctx.xml")
public class InMemoryApplicationSessionServiceTest {

    @Autowired
    private SessionIdProvider mockSessionIdProvider;

    @Autowired
    private ApplicationSessionService service;

    @Autowired
    private ServiceSessionDefinition serviceSessionDefinition;

    @Test
    public void givenSessionDefinitionWhenCreateSessionThenSessionInstanceIsCreated() {
        givenWeHaveSessionDefinition();

        ApplicationSession session = whenCreateNewSession();

        ServiceSession serviceSession = thenItIsServiceSession(session);
    }

    @Test
    public void givenIdOfExistingSessionThenTheSessionCanBeRetrievedById() {
        givenWeHaveSessionDefinition();
        givenSessionIdProvider("12345");
        ApplicationSession existingSession = givenCreatedSession();
        
        ApplicationSession session = whenRetrieveSessionbyId("12345");
        
        thenTheSessionCorrespondsToExistingOne(session, existingSession);
    }

    private void thenTheSessionCorrespondsToExistingOne(ApplicationSession session, ApplicationSession existingSession) {
        assertThat(session.getId(), is(existingSession.getId()));
    }

    private ApplicationSession whenRetrieveSessionbyId(String sessionId) {
        return service.getSessionById(sessionId);
    }

    private ApplicationSession givenCreatedSession() {
        return service.createNewSession(serviceSessionDefinition);
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

    private void givenSessionIdProvider(String expectedSessionId) {
        when(mockSessionIdProvider.getSessionId()).thenReturn(expectedSessionId);
    }
}
