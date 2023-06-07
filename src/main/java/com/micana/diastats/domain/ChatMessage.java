package com.micana.diastats.domain;

import javax.persistence.*;

/**
 * Created by rajeevkumarsingh on 24/07/17.
 */
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private MessageType type;
    private String content;
    private String sender;

    private String rerecipientt;



    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;


    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public User getRecipient() {
        return recipient;
    }

    public String getRerecipientt() {
        return rerecipientt;
    }

    public void setRerecipientt(String rerecipientt) {
        this.rerecipientt = rerecipientt;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
}
