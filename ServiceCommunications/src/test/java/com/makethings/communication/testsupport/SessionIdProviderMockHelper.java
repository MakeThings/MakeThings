package com.makethings.communication.testsupport;

import static org.mockito.Mockito.when;

import com.makethings.communication.session.SessionIdProvider;

public class SessionIdProviderMockHelper {
    private SessionIdProvider sessionIdProviderMock;

    public SessionIdProviderMockHelper(SessionIdProvider sessionIdProviderMock) {
        this.sessionIdProviderMock = sessionIdProviderMock;
    }
    
    public void givenSessionIdProvider(String expectedSessionId) {
        when(sessionIdProviderMock.getSessionId()).thenReturn(expectedSessionId);
    }
}
