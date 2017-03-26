package com.vksinghh.controllers;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: vksinghh
 * Date: 3/26/17 6:53 PM
 */
@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Scheduled(fixedRate = 1000)
    public void writeToQueue() {
        AmazonSQS sqsClient = AmazonSQSClientBuilder.standard().withCredentials(new ProfileCredentialsProvider()).withRegion(Regions.US_EAST_1).build();
        GetQueueUrlResult getQueueUrlResult = sqsClient.getQueueUrl("vksinghhqueue");

        String newMessage = "This is a random message " + (long) (Math.random() * 1000);
        SendMessageRequest sendMessageRequest = new SendMessageRequest(getQueueUrlResult.getQueueUrl(), newMessage);

        log.info("Sending msg to queue : " + newMessage);
        log.info("queueUrl : " + getQueueUrlResult.getQueueUrl());
        log.info("Thread Name : " + Thread.currentThread().getName());
	sqsClient.sendMessage(sendMessageRequest);
    }

    @Scheduled(fixedRate = 5000)
    public void readFromQueue() {
        AmazonSQS sqsClient = AmazonSQSClientBuilder.standard().withCredentials(new ProfileCredentialsProvider()).withRegion(Regions.US_EAST_1).build();
        GetQueueUrlResult getQueueUrlResult = sqsClient.getQueueUrl("vksinghhqueue");

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(getQueueUrlResult.getQueueUrl()).withMaxNumberOfMessages(5);
        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).getMessages();
        for (Message message : messages) {
            log.info("  Message");
            log.info("    MessageId:     " + message.getMessageId());
            log.info("    Body:          " + message.getBody());
        }
	log.info("Thread Name : " + Thread.currentThread().getName());

    }
}
