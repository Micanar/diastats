package com.micana.diastats.controller;


import com.micana.diastats.domain.ChatMessage;
import com.micana.diastats.domain.User;
import com.micana.diastats.repos.MessageRepo;
import com.micana.diastats.service.UserSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by rajeevkumarsingh on 24/07/17.
 */

@Controller
@EnableWebSecurity
public class ChatController {
    @Autowired
    MessageRepo messageRepo;

    String username;



    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public List<ChatMessage> sendMessage(Authentication authentication, @Payload ChatMessage chatMessage) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userDetails;
        if (user.getDoctor()!=null){
            chatMessage.setRecipient(user.getDoctor());
        }

        username = authentication.getName();
        chatMessage.setSender(username);
        messageRepo.save(chatMessage);
        System.out.println(chatMessage.getRecipient());


        return messageRepo.findChatMessageBySender(user.getUsername());

    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username to WebSocket session attributes
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        headerAccessor.getSessionAttributes().put("username", user.getUsername());
        username = user.getUsername(); // Set username for the controller
        return chatMessage;
    }

    @MessageMapping("/chat.getMessages")
    @SendTo("/topic/public")
    public List<ChatMessage> getMessages(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userDetails;

        return messageRepo.findAll();

    }

    @GetMapping("/chat")
    public String chat() {
        return "/index";
    }
}
