package com.example.service;

import com.example.repository.MessageRepository;

import java.lang.module.ResolutionException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.entity.Message;
import com.example.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

        MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository)
    {
        this.messageRepository = messageRepository;
    }

    public List<Message> getMessageList()
    {
        return (List<Message>) this.messageRepository.findAll();
    }

    public List<Message> getMessageListByAccountId(Integer accountId) throws ResourceNotFoundException
    {
        return this.messageRepository.findByPostedBy(accountId).orElseThrow(() -> new ResourceNotFoundException("Messages not found under ID: " + accountId));
    }

    public Message findMessage(Integer messageId) throws ResourceNotFoundException
    {
        return this.messageRepository.findById(messageId).orElseThrow(() -> new ResourceNotFoundException("Message with ID: " + messageId + " not found"));
    }

    public void saveMessage(Message newMessage)
    {
        this.messageRepository.save(newMessage);
    }

    public void deleteMessage(Integer messageId) throws IllegalArgumentException
    {
        this.messageRepository.deleteById(messageId);
    }


}
