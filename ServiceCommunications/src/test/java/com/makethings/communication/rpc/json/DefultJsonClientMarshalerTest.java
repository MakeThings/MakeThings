package com.makethings.communication.rpc.json;

import static org.hamcrest.CoreMatchers.equalTo;

import java.lang.reflect.Method;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import com.jayway.jsonassert.JsonAssert;
import com.jayway.jsonassert.JsonAsserter;

public class DefultJsonClientMarshalerTest {

    private JsonClientRequest result;
    private Method method;
    private Object[] args;
    private DefultJsonClientMarshaler defultJsonClientMarshaler;
    private static final String SESSION_ID = "SessionID";
    
    
    @Before
    public void setUp() {
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

    private void thenClientRequestHasSessionId() {
        JsonAsserter json = JsonAssert.with(result.getMessage());
        json.assertThat("$.SId", equalTo(SESSION_ID));
    }

    private void whenMarshalRequest() {
        result = defultJsonClientMarshaler.marshalClientRequest(SESSION_ID, method, args);
    }

    private void givenMethodNameAndArguments() throws SecurityException, NoSuchMethodException {
        method = TestIdeaService.class.getDeclaredMethod("createNewIdea", String.class);
        args = new Object[] { "foo" };
    }

    private void thenClientRequestHasJsonRpcMessage() throws JSONException {
        JsonAsserter json = JsonAssert.with(result.getMessage());
        json.assertThat("$.Req.method", equalTo("createNewIdea"));
        json.assertThat("$.Req.params.name", equalTo("foo"));
    }
}
