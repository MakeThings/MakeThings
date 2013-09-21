package com.makethings.communication.rpc.json;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.commons.io.FileUtils;
import org.hamcrest.CoreMatchers;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jayway.jsonassert.JsonAssert;
import com.jayway.jsonassert.JsonAsserter;

public class DefultJsonClientMarshalerTest {

    private JsonClientRequest request;
    private Method method;
    private Object[] args;
    private DefultJsonClientMarshaler defultJsonClientMarshaler;
    private JsonClientResponse jsonResponse;
    private static final String SESSION_ID = "SessionID";
    private Object serviceMethodResult;
    
    @Before
    public void setUp() throws NoSuchMethodException, SecurityException {
        method = TestIdeaService.class.getDeclaredMethod("createNewIdea", String.class);
        defultJsonClientMarshaler = new DefultJsonClientMarshaler();
    }

    @Test
    public void marshalMethodNameAndArguments() throws SecurityException, NoSuchMethodException, JSONException {
        givenMethodNameAndArguments();
        whenMarshalRequest();
        thenClientRequestHasJsonRpcMessage();
    }
    
    @Test
    public void marshalSessionId() throws SecurityException, NoSuchMethodException {
        givenMethodNameAndArguments();
        whenMarshalRequest();
        thenClientRequestHasSessionId();
    }
    
    @Test
    public void marshalRequestId() throws SecurityException, NoSuchMethodException {
        givenMethodNameAndArguments();
        whenMarshalRequest();
        thenClientRequestHasId();
    }
    
    @Test
    public void populateInvokedMethodIntoRequestObjectWhenMarshal() throws SecurityException, NoSuchMethodException {
        givenMethodNameAndArguments();
        whenMarshalRequest();
        thenMethodPopulatedToRequestObject();
    }
    
    @Test
    public void demarshalServiceJsonReponse() throws IOException {
        givenJsonResponse();
        
        whenDemarshal();
        
        thenServiceMethodCallResultIs(100);
    }
    
    private void thenServiceMethodCallResultIs(Integer expectedResult) {
        assertThat(serviceMethodResult, instanceOf(Integer.class));
        assertThat((Integer)serviceMethodResult, CoreMatchers.is(expectedResult));
    }

    private void whenDemarshal() {
        serviceMethodResult = defultJsonClientMarshaler.demarshalClientResponse(jsonResponse);
    }

    private void givenJsonResponse() throws IOException {
        String serivceResponseFilePath = DefultJsonClientMarshalerTest.class.getResource("/json/createIdeaServiceResponse.txt").getPath();
        File serviceResponseFile = new File(serivceResponseFilePath);
        String serviceResponseMessage = FileUtils.readFileToString(serviceResponseFile);
        ClientResponseMessageWrapper serviceResponseWrapper = new ClientResponseMessageWrapper(serviceResponseMessage);
        jsonResponse = new JsonClientResponse(serviceResponseWrapper, method);
    }

    private void thenMethodPopulatedToRequestObject() {
        Assert.assertThat(method, CoreMatchers.is(request.getMethod()));
    }

    private void thenClientRequestHasId() {
        JsonAsserter json = JsonAssert.with(request.getMessage());
        json.assertThat("$.Req.id", equalTo(request.getRequestId()));
    }

    private void thenClientRequestHasSessionId() {
        JsonAsserter json = JsonAssert.with(request.getMessage());
        json.assertThat("$.SId", equalTo(SESSION_ID));
    }

    private void whenMarshalRequest() {
        request = defultJsonClientMarshaler.marshalClientRequest(SESSION_ID, method, args);
    }

    private void givenMethodNameAndArguments() throws SecurityException, NoSuchMethodException {
        assertNotNull(method);
        args = new Object[] { "foo" };
    }

    private void thenClientRequestHasJsonRpcMessage() throws JSONException {
        JsonAsserter json = JsonAssert.with(request.getMessage());
        json.assertThat("$.Req.method", equalTo("createNewIdea"));
        json.assertThat("$.Req.params.name", equalTo("foo"));
    }
}
