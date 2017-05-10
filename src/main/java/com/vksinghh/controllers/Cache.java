package com.vksinghh.controllers;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by vipkumar on 11/05/17.
 */
@Component
public class Cache {

    private static Set<String> messages;

    static {
        messages = new HashSet<>();
    }

    public int getSize() {
        return messages.size();
    }

    public void addMessage(String messageId) {
        messages.add(messageId);
    }

    public boolean contains(String messageId) {
        return messages.contains(messageId);
    }
}
