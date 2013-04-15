package com.makethings.communication.rpc.sqs;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.makethings.communication.rpc.Queue;

public interface SqsQueue extends Queue {

    void setAwsCredentials(AWSCredentials credentials);

    SendMessageResult sendMessage(SendMessageRequest messageRequest);

    ReceiveMessageResult receiveMessage(ReceiveMessageRequest receiveMessageRequest);
    
}
