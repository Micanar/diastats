package com.micana.diastats.controller;

import com.micana.diastats.domain.ChatMessage;

import java.util.Comparator;

public class ChatMessageComparator implements Comparator<ChatMessage> {
    @Override
    public int compare(ChatMessage message1, ChatMessage message2) {
        return message1.getTimestamp().compareTo(message2.getTimestamp());
    }
}
