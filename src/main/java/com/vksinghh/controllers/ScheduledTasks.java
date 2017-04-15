package com.vksinghh.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * User: vksinghh
 * Date: 3/26/17 6:53 PM
 */
@Component
public class ScheduledTasks {

    @Autowired
    private Writer writer;

    @Autowired
    private Reader reader;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Scheduled(fixedDelay = 3000)
    public void writeTask() {
        log.info("Spawning multiple Write Asyc tasks from thread : " + Thread.currentThread().getName());
        for (int i = 0; i < 10; i++) {
            writer.writeToQueue();
        }
    }

    @Scheduled(fixedDelay = 3000)
    public void readTask() {
        log.info("Spawning multiple Read Asyc tasks from thread : " + Thread.currentThread().getName());
        for (int i = 0; i < 10; i++) {
            reader.readFromQueue();
        }
    }

}
