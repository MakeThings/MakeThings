package com.makethings.communication.session.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.makethings.communication.queue.QueueServiceCredentials;
import com.makethings.communication.queue.QueueServiceCredentialsProvider;
import com.makethings.communication.session.ApplicationSession;
import com.makethings.communication.session.ApplicationSessionService;
import com.makethings.communication.session.CreateSessionException;
import com.makethings.communication.session.SessionIdProvider;
import com.makethings.communication.session.SessionNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/InMemoryApplicationSessionServiceTest.xml")
public class InMemoryApplicationSessionServiceTest {

    private static final String SESSION_ID = "12345";

    @Autowired
    private SessionIdProvider mockSessionIdProvider;

    @Autowired
    private ApplicationSessionService service;

    @Autowired
    private ServiceSessionDefinition serviceSessionDefinition;

    @Autowired
    private QueueServiceCredentialsProvider<QueueServiceCredentials> credentialsProvider;

    @Rule
    public ExpectedException throwException = ExpectedException.none();

    @Before
    public void setUp() {
        Mockito.reset(credentialsProvider);
    }
    
    @Test
    public void givenSessionDefinitionWhenCreateSessionThenSessionInstanceIsCreated() {
        givenWeHaveSessionDefinition();

        ApplicationSession session = whenCreateNewSession();

        ServiceSession serviceSession = thenItIsServiceSession(session);
    }

    @Test
    public void givenExceptionWhenCreateASessionThenCreateSessionExceptionShouldBeThrown() {
        throwException.expect(CreateSessionException.class);
        givenWeHaveSessionDefinition();
        givenExceptionWhenRequestCredentials();

        ApplicationSession session = whenCreateNewSession();

    }

    private void givenExceptionWhenRequestCredentials() {
        when(credentialsProvider.getCredentials()).thenThrow(new RuntimeException("Error request credentials"));
    }

    @Test
    @DirtiesContext
    public void givenIdOfExistingSessionThenTheSessionCanBeRetrievedById() {
        givenWeHaveSessionDefinition();
        givenSessionIdProvider(SESSION_ID);
        ApplicationSession existingSession = givenCreatedSession();

        ApplicationSession session = whenRetrieveSessionbyId(SESSION_ID);

        thenTheSessionCorrespondsToExistingOne(session, existingSession);
    }

    @Test
    @DirtiesContext
    public void givenWrongIdWhenGettingSessionThenExceptionShouldBeThrown() {
        expectThatSessionNotFoundExWillBeThrown();

        whenRetrieveSessionbyId(SESSION_ID);
    }

    @Test
    @DirtiesContext
    public void givenIdOfExistingSessionWhenDeleteSessionThenItCannotBeFoundAnyMore() {
        givenWeHaveSessionDefinition();
        givenSessionIdProvider(SESSION_ID);
        ApplicationSession existingSession = givenCreatedSession();

        whenDeleteSessionById(SESSION_ID);

        thenThereIsNoSessionWithId(SESSION_ID);
    }

    private void expectThatSessionNotFoundExWillBeThrown() {
        throwException.expect(SessionNotFoundException.class);
        throwException.expectMessage(SESSION_ID);
    }

    private void thenThereIsNoSessionWithId(String sessionId) {
        expectThatSessionNotFoundExWillBeThrown();
        service.getSessionById(sessionId);
    }

    private void whenDeleteSessionById(String sessionId) {
        service.deleteSessionById(sessionId);
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
