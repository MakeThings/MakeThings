package com.makethings.communication.rpc;

import org.mockito.Mockito;


public class TestRemoteServiceHelper {
    private TestRemoteService mockService;

    public TestRemoteServiceHelper(TestRemoteService mockService) {
        this.mockService = mockService;
    }

    public void expextStartProcessing() {
        Mockito.doNothing().when(mockService).startProcessing();
    }

    public void thenProcessingIsStarted() {
        Mockito.verify(mockService).startProcessing();
    }
}
