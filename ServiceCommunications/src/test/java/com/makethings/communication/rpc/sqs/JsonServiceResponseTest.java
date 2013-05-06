package com.makethings.communication.rpc.sqs;

import java.io.IOException;
import java.io.OutputStreamWriter;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.makethings.communication.rpc.json.JsonServiceResponse;
import com.makethings.communication.support.FileHelper;

public class JsonServiceResponseTest {
    private static final String RPC_RESPONSE_FILENAME = "/json/createIdeaJsonResponse.txt";
    private static final String SERVICE_RESPONSE_FILENAME = "/json/createIdeaServiceResponse.txt";
    private JsonServiceResponse response;
    private String message; 
    
    @Test
    public void givenJsonRpcResponseWhenGetMessageThenServiceResponseShouldBeFormatted() throws IOException {
        givenJsonRpcResponse();
    
        whenGetMessage();
    
        thenMessageIsServiceResponse();
    }

    private void thenMessageIsServiceResponse() {
        String expectedServiceReseponse = FileHelper.readFromFilename(SERVICE_RESPONSE_FILENAME);
        Assert.assertThat(message, CoreMatchers.is(expectedServiceReseponse));
    }

    private void whenGetMessage() {
        message = response.getMessage();
    }

    private void givenJsonRpcResponse() throws IOException {
        response = new JsonServiceResponse();
        String rpc = FileHelper.readFromFilename(RPC_RESPONSE_FILENAME);
        OutputStreamWriter ow = new OutputStreamWriter(response.getOutputStream());
        ow.write(rpc);
        ow.close();
    }
}
