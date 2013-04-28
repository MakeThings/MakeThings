package com.makethings.communication.rpc.sqs;

import java.util.concurrent.Executor;

import com.amazonaws.services.sqs.model.Message;
import com.makethings.communication.rpc.json.JsonRpcHandler;
import com.makethings.communication.rpc.json.JsonRpcRequest;

public class RequestHandlingTask implements Runnable {

    private JsonRpcHandler jsonRpcHandler;
    private Message message;

    public RequestHandlingTask withMessage(Message message) {
        this.message = message;
        return this;
    }

    @Override
    public void run() {
        jsonRpcHandler.handle(new JsonRpcRequest().withMessages(message.getBody()));
    }

    public void execute(Executor requestProcessingExecutor) {
        requestProcessingExecutor.execute(this);
    }

    public RequestHandlingTask withHandler(JsonRpcHandler jsonRpcHandler) {
        this.jsonRpcHandler = jsonRpcHandler;
        return this;
    }

}
