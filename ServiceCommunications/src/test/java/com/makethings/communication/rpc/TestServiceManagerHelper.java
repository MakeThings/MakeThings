package com.makethings.communication.rpc;

import org.mockito.Matchers;
import org.mockito.Mockito;

import com.makethings.communication.session.service.ServiceSession;
import com.makethings.communication.session.service.ServiceSessionDefinition;

public class TestServiceManagerHelper {
    private ServiceManager serviceManager;

    public TestServiceManagerHelper(ServiceManager serviceManager) {
        super();
        this.serviceManager = serviceManager;
    }

    public void givenCreatedSessionByDefinition(ServiceSession serviceSession, ServiceSessionDefinition sessionDefinition) {
        Mockito.when(serviceManager.openServiceSession(Mockito.same(sessionDefinition))).thenReturn(serviceSession);
    }

    public void thenSessionIsCreatedByDefinition(ServiceSessionDefinition sessionDefinition) {
        Mockito.verify(serviceManager).openServiceSession(Mockito.same(sessionDefinition));
    }

    public void thenSessionIsClosed(ServiceSession serviceSession) {
        Mockito.verify(serviceManager, Mockito.timeout(5 * 1000)).closeServiceSession(Matchers.eq(serviceSession.getId()));
    }
    
    public void givenExceptionWhenOpenSession() {
        Mockito.when(serviceManager.openServiceSession(Matchers.isA(ServiceSessionDefinition.class))).thenThrow(new RuntimeException("qwefqwfe"));
    }

}
