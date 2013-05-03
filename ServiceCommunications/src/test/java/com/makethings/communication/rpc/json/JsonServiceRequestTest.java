package com.makethings.communication.rpc.json;

import java.io.IOException;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.makethings.communication.support.FileHelper;

public class JsonServiceRequestTest {

    private JsonServiceRequest request;
    private JsonNode jsonNode;

    @Test
    public void givenServiceRequestWhenGetRpcRequestThenItContainsReq() throws IOException {
        givenServiceRequest();

        whenGetRpcRequest();

        thenItContainsReq();
    }

    private void thenItContainsReq() throws IOException {
        Assert.assertThat(jsonNode.toString(), CoreMatchers.is(FileHelper.readFromFilename("/json/createIdeaRpcRequest.txt")));
    }

    private void whenGetRpcRequest() {
        jsonNode = request.getRpcRequest();
    }

    private void givenServiceRequest() throws IOException {
        request = new JsonServiceRequest().withMessages(FileHelper.readFromFilename("/json/createIdeaServiceRequest.txt"));
    }
}
