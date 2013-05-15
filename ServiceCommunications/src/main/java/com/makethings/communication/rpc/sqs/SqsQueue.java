package com.makethings.communication.rpc.sqs;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.makethings.communication.rpc.Queue;
import com.makethings.communication.rpc.QueueException;

public interface SqsQueue extends Queue {

    void setAwsCredentials(AWSCredentials credentials);

    SendMessageResult sendMessage(SendMessageRequest messageRequest) throws QueueException;

    ReceiveMessageResult receiveMessage(ReceiveMessageRequest receiveMessageRequest) throws QueueException;

    void deleteMessage(DeleteMessageRequest deleteMessageRequest);
    
    CreateQueueResult createQueue(CreateQueueRequest request) throws QueueException;
    
    
    
}
