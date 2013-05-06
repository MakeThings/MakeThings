package com.makethings.communication.rpc.json;

import static com.makethings.communication.support.FileHelper.readFromFilename;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultJsonRpcHandlerTest {

    private JsonServiceRequest request;
    private DefaultJsonRpcHandler handler;

    @Mock
    private TestIdeaService ideaService;

    private JsonServiceResponse response;

    @Before
    public void setUp() {
        handler = new DefaultJsonRpcHandler();
        handler.setService(ideaService);
        handler.setServiceInterfaceClass(TestIdeaService.class);

        response = new JsonServiceResponse();
    }

    @Test
    public void givenJsonRequestWhenHandleThenServiceMethodIsCalled() throws IOException {
        givenJsonRequest();

        whenHandlingRequest();

        thenCreateIdeamMethodIsInvoked();
    }

    @Test
    public void givenResultOfServiceMethodWhenHandlingThenItShouldBeMarshaledIntoJson() throws IOException {
        givenJsonRequest();
        givenServiceMethodResult();

        whenHandlingRequest();

        thenResultOfMethodCallIsMarshaledIntoJson();
    }

    private void thenResultOfMethodCallIsMarshaledIntoJson() throws IOException {
        String actualJson = response.getOutputStream().toString();
        Assert.assertThat(actualJson, CoreMatchers.equalTo(readFromFilename("/json/createIdeaJsonResponse.txt")));
    }

    private void givenServiceMethodResult() {
        Mockito.when(ideaService.createNewIdea(eq("foo"))).thenReturn(100);
    }

    private void thenCreateIdeamMethodIsInvoked() {
        verify(ideaService).createNewIdea(eq("foo"));
    }

    private void whenHandlingRequest() {
        handler.handle(request, response);
    }

    private void givenJsonRequest() throws IOException {
        request = new JsonServiceRequest().withMessages(readFromFilename("/json/createIdeaServiceRequest.txt"));
    }

}
