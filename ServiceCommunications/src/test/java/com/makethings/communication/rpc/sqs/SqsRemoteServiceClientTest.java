package com.makethings.communication.rpc.sqs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.makethings.communication.rpc.ClientManager;
import com.makethings.communication.session.user.DefaultUserSessionDefinition;

@RunWith(MockitoJUnitRunner.class)
public class SqsRemoteServiceClientTest {
    
    private DefaultUserSessionDefinition sessionDefinition;
    private SqsRemoteServiceClient serviceClient;
    
    @Mock
    private ClientManager clientManager;

    @Before
    public void setUp() {
        serviceClient = new SqsRemoteServiceClient();
        serviceClient.setClientManaget(clientManager);
    }
    
    @Test
    public void givenUserSessionDefinitionWhenInitThenSessionIsCreated() {
        givenUserSessionDefinition();
        
        whenInit();
        
        thenSessionIsCreated();
    }

    private void thenSessionIsCreated() {
        Mockito.verify(clientManager).openClientSession(Mockito.same(sessionDefinition));
    }

    private void whenInit() {
        serviceClient.init();
    }

    private void givenUserSessionDefinition() {
        sessionDefinition = new DefaultUserSessionDefinition();
        serviceClient.setUserSessionDefinition(sessionDefinition);
        
    }
}
