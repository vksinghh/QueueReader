package com.vksinghh.controllers;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * User: vksinghh
 * Date: 4/15/17 1:10 PM
 */
@Component
public class Writer {

    private static final Logger log = LoggerFactory.getLogger(Writer.class);

    @Async("myWriteExecutor")
    public void writeToQueue() {
        AmazonSQS sqsClient = AmazonSQSClientBuilder.standard().withCredentials(new ProfileCredentialsProvider()).withRegion(Regions.US_EAST_1).build();
        GetQueueUrlResult getQueueUrlResult = sqsClient.getQueueUrl("vksinghhqueue");

        String newMessage = "This is a random message " + (long) (Math.random() * 1000);
        SendMessageRequest sendMessageRequest = new SendMessageRequest(getQueueUrlResult.getQueueUrl(), newMessage);

        log.info("Start Sending message through thread : " + Thread.currentThread().getName());
        sqsClient.sendMessage(sendMessageRequest);
        log.info("End Sending message through thread : " + Thread.currentThread().getName());
    }
}
