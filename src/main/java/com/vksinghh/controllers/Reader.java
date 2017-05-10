package com.vksinghh.controllers;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: vksinghh
 * Date: 4/15/17 1:09 PM
 */
@Component
public class Reader {

    private static final Logger log = LoggerFactory.getLogger(Reader.class);

    @Autowired
    private Cache cache;

    @Async("myReadExecutor")
    public void readFromQueue() {
        AmazonSQS sqsClient = AmazonSQSClientBuilder.standard().withCredentials(new ProfileCredentialsProvider()).withRegion(Regions.US_EAST_1).build();
        GetQueueUrlResult getQueueUrlResult = sqsClient.getQueueUrl("vksinghhqueue");

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(getQueueUrlResult.getQueueUrl()).withMaxNumberOfMessages(5);

        log.info("Start Receiving messages through thread : " + Thread.currentThread().getName());
        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).getMessages();
        for (Message message : messages) {

            if (cache.contains(message.getMessageId())) {
                System.out.println("*** Alert : Duplicate seen");
            }

            System.out.println("*** Cache Size : " + cache.getSize());

            System.out.println("********" + message.getMessageId() + "*********");
            DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest(getQueueUrlResult.getQueueUrl(), message.getReceiptHandle());
            sqsClient.deleteMessage(deleteMessageRequest);


            // all deleted messages go into the queue
            cache.addMessage(message.getMessageId());
        }
        log.info("End Receiving messages through thread : " + Thread.currentThread().getName());
    }
}