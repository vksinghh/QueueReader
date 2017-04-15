package com.vksinghh.controllers;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * User: vksinghh
 * Date: 3/26/17 6:45 PM
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskScheduler() {
        return Executors.newScheduledThreadPool(5);
    }

    @Bean
    @Qualifier("myReadExecutor")
    public TaskExecutor readTaskExecutor() {
        ThreadFactory readThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("read-pool-%d").build();
        return new ConcurrentTaskExecutor(
                Executors.newFixedThreadPool(3, readThreadFactory));
    }

    @Bean
    @Qualifier("myWriteExecutor")
    public TaskExecutor writeTaskExecutor() {
        ThreadFactory writeThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("write-pool-%d").build();
        return new ConcurrentTaskExecutor(
                Executors.newFixedThreadPool(3, writeThreadFactory));
    }
}
