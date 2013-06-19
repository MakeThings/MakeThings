package com.makethings.communication.rpc.json;

import static org.hamcrest.CoreMatchers.equalTo;

import java.lang.reflect.Method;

import org.hamcrest.CoreMatchers;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.jayway.jsonassert.JsonAssert;
import com.jayway.jsonassert.JsonAsserter;



public class DefultJsonClientMarshalerTest {

    private JsonClientRequest result;
    private Method method;
    private Object[] args;
    private DefultJsonClientMarshaler defultJsonClientMarshaler;
    
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

    private void whenMarshalRequest() {
        result = defultJsonClientMarshaler.marshalClientRequest(method, args);
    }

    private void givenMethodNameAndArguments() throws SecurityException, NoSuchMethodException {
        method = TestIdeaService.class.getDeclaredMethod("createNewIdea", String.class);
        args = new Object[] { "foo" };
    }

    private void thenClientRequestHasJsonRpcMessage() throws JSONException {
        System.out.println(result.getMessage());
        JsonAsserter jsonAsserter = JsonAssert.with(result.getMessage());
        jsonAsserter.assertThat("$.Req.method", equalTo("createNewIdea"));
        jsonAsserter.assertThat("$.Req.params.name", equalTo("foo"));
    }
}
