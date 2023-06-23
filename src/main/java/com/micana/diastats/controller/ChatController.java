package com.micana.diastats.controller;


import com.micana.diastats.domain.ChatMessage;
import com.micana.diastats.domain.Role;
import com.micana.diastats.domain.User;
import com.micana.diastats.repos.MessageRepo;
import com.micana.diastats.repos.UserRepo;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by rajeevkumarsingh on 24/07/17.
 */

@Controller
@EnableWebSecurity
public class ChatController {
    @Autowired
    MessageRepo messageRepo;

    @Autowired
    UserRepo userRepo;



    String username;

    Long patientID;



    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public List<ChatMessage> sendMessage(Authentication authentication, @Payload ChatMessage chatMessage) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userDetails;
        if (user.getDoctor()!=null){
            User doctor = user.getDoctor();
            List<ChatMessage> doctorMessages=messageRepo.findChatMessageBySenderAndRecipient(user.getDoctor().getUsername(),user);
            chatMessage.setRecipient(user.getDoctor());
            username = authentication.getName();
            chatMessage.setSender(username);
            chatMessage.setTimestamp(LocalDateTime.now());
            messageRepo.save(chatMessage);
            System.out.println(chatMessage.getRecipient());


            List<ChatMessage> messages= messageRepo.findChatMessageBySender(user.getUsername());


            messages.addAll(doctorMessages);
            ChatMessageComparator comparator = new ChatMessageComparator();
            Collections.sort(messages, comparator);
            return messages;
        }
        if (user.getRoles().contains(Role.DOCTOR)){
            User res = userRepo.findById(patientID).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + patientID));
            chatMessage.setRecipient(res);
        }

        username = authentication.getName();
        chatMessage.setSender(username);
        chatMessage.setTimestamp(LocalDateTime.now());
        messageRepo.save(chatMessage);
        System.out.println(chatMessage.getRecipient());
        if (user.getRoles().contains(Role.DOCTOR)) {
            User res = userRepo.findById(patientID).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + patientID));
            List<ChatMessage> doctormes = messageRepo.findChatMessageBySenderAndRecipient(user.getUsername(),res);
            List<ChatMessage> patientmes = messageRepo.findChatMessageBySender(res.getUsername());
            doctormes.addAll(patientmes);
            ChatMessageComparator comparator = new ChatMessageComparator();
            Collections.sort(doctormes, comparator);
            System.out.println(res);
            return doctormes;
        }

        List<ChatMessage> messages= messageRepo.findChatMessageBySender(user.getUsername());



        ChatMessageComparator comparator = new ChatMessageComparator();
        Collections.sort(messages, comparator);

        return messages;

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
        if (user.getRoles().contains(Role.DOCTOR)){
            System.out.println(patientID);
            User res = userRepo.findById(patientID).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + patientID));
            List<ChatMessage> doctormes = messageRepo.findChatMessageBySenderAndRecipient(user.getUsername(),res);
            List<ChatMessage> patientmes = messageRepo.findChatMessageBySender(res.getUsername());
            doctormes.addAll(patientmes);
            ChatMessageComparator comparator = new ChatMessageComparator();
            Collections.sort(doctormes, comparator);

            return doctormes;
        }

        List<ChatMessage> messages= messageRepo.findChatMessageBySender(user.getUsername());
        List<ChatMessage> doctorMessages=messageRepo.findChatMessageBySenderAndRecipient(user.getDoctor().getUsername(),user);
        messages.addAll(doctorMessages);
        ChatMessageComparator comparator = new ChatMessageComparator();
        Collections.sort(messages, comparator);

        return messages;

    }

    @GetMapping("/chat")
    public String chat() {
        return "/index";
    }
    @GetMapping("/users/{userId}/chat")
    public String chatUser(@PathVariable Long userId, Model model, Principal principal){
        patientID=userId;
        System.out.println(userRepo.findById(patientID));


        return "/index";
    }



}
