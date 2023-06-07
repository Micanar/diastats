package com.micana.diastats.repos;


import com.micana.diastats.domain.ChatMessage;
import com.micana.diastats.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepo extends JpaRepository<ChatMessage,Long> {
    List<ChatMessage> findAll();
    List<ChatMessage> findChatMessageBySender (String sender);
    List<ChatMessage> findChatMessageByRecipient(User user);
    List<ChatMessage> findChatMessageBySenderAndRecipient (String sender,User recipientid);
}
