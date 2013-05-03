package com.makethings.communication.rpc.json;

import static com.makethings.communication.support.FileHelper.readFromFilename;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultJsonRpcHandlerTest {

    private JsonRpcRequest request;
    private DefaultJsonRpcHandler handler;
    
    @Mock
    private TestIdeaService ideaService;

    private JsonRpcResponse response;
    
    @Before
    public void setUp() {
        handler = new DefaultJsonRpcHandler();
        handler.setService(ideaService);
        handler.setServiceInterfaceClass(TestIdeaService.class);
    
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        response = new JsonRpcResponse().withOutputStream(baos);
    }

    @Test
    public void givenJsonRequestWhenHandleThenServiceMethodIsCalled() throws IOException {
        givenJsonRequest();

        whenHandlingRequest();
        
        thenCreateIdeamMethodIsInvoked();
    }

    private void thenCreateIdeamMethodIsInvoked() {
        verify(ideaService).createNewIdea(eq("foo"));
    }

    private void whenHandlingRequest() {
        handler.handle(request, response);
    }

    private void givenJsonRequest() throws IOException {
        request = new JsonRpcRequest().withMessages(readFromFilename("/json/createIdeaRequest.txt"));
    }

}
