package com.makethings.communication.rpc;

import org.junit.rules.ExpectedException;
import org.mockito.Mockito;


public class TestRemoteServiceHelper {
    private final TestRemoteService mockService;
    private final ExpectedException expectedException;

    public TestRemoteServiceHelper(TestRemoteService mockService, ExpectedException expectedException) {
        this.mockService = mockService;
        this.expectedException = expectedException;
    }

    public void expextStartProcessing() {
        Mockito.doNothing().when(mockService).startProcessing();
    }

    public void thenProcessingIsStarted() {
        Mockito.verify(mockService, Mockito.timeout(1000)).startProcessing();
    }

    public void expectRemoteServiceException() {
        expectedException.expect(RemoteServiceException.class);
    }
}
