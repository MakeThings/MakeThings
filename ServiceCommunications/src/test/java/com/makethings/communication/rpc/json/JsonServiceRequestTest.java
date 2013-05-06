package com.makethings.communication.rpc.json;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.makethings.communication.support.FileHelper;

public class JsonServiceRequestTest {

    private JsonServiceRequest request;
    private JsonNode jsonNode;
    private String clientSessionId;

    @Test
    public void givenServiceRequestWhenGetRpcRequestThenItContainsReq() throws IOException {
        givenServiceRequest();

        whenGetRpcRequest();

        thenItContainsReq();
    }
    
    @Test
    public void givenServiceRequestWhenGetClientSessionSessionIdThenItReturnsValueFromTheRequest() throws IOException {
        givenServiceRequest();

        whenGetClientSessionId();
        
        thenClientSessionIdIs200();
    }

    private void thenClientSessionIdIs200() {
        assertThat(clientSessionId, is("200"));
    }

    private void whenGetClientSessionId() {
        clientSessionId = request.getClientSessionId();
    }

    private void thenItContainsReq() throws IOException {
        assertThat(jsonNode.toString(), is(FileHelper.readFromFilename("/json/createIdeaRpcRequest.txt")));
    }

    private void whenGetRpcRequest() {
        jsonNode = request.getRpcRequest();
    }

    private void givenServiceRequest() throws IOException {
        request = new JsonServiceRequest().withMessages(FileHelper.readFromFilename("/json/createIdeaServiceRequest.txt"));
    }
}
