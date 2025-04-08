package com.example.service;

import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class MessageService {

        MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository)
    {
        this.messageRepository = messageRepository;
    }
}
