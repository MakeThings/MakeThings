package com.makethings.communication.rpc;

import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import com.makethings.commons.exeption.AppRuntimeExeption;

public class TestRemoteServiceHelper {
    private final TestRemoteService mockService;
    private final ExpectedException expectedException;

    public TestRemoteServiceHelper(TestRemoteService mockService, ExpectedException expectedException) {
        this.mockService = mockService;
        this.expectedException = expectedException;
    }

    public void expextStartProcessing() {
        Mockito.doNothing().when(mockService).processing();
    }

    public void thenProcessingIsStarted() {
        Mockito.verify(mockService, Mockito.timeout(1000)).processing();
    }

    public void expectRemoteServiceException() {
        expectedException.expect(RemoteServiceException.class);
    }

    public void givenErrorWhenProcessing() {
        Mockito.doThrow(AppRuntimeExeption.class).when(mockService).processing();
    }
}
