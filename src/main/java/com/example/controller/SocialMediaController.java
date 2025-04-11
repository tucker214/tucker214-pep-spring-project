package com.example.controller;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.ResourceNotFoundException;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

import com.example.service.AccountService;
import com.example.service.MessageService;

 @RestController
 @RequestMapping()
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService)
    {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("register")
    public @ResponseBody ResponseEntity<Account> registerAccount(@RequestBody Account newAccount)
    {

        if (!newAccount.getUsername().isEmpty() && newAccount.getPassword().length() > 3)
        {
            try {
                boolean doesUsernameExist = this.accountService.doesUsernameExist(newAccount.getUsername());
                if (doesUsernameExist)
                {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(newAccount);
                }
            } catch (ResourceNotFoundException e) {
                //duplicate username not found, so resource returns a ResourceNotFound exception. Account successfully created
                e.getMessage();
                this.accountService.registerAccount(newAccount);
                return ResponseEntity.status(HttpStatus.OK).body(newAccount);
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PostMapping("login")
    public @ResponseBody ResponseEntity<Account> login(@RequestBody Account account)
    {
        Account loggedInAccount = null;

        try {
            loggedInAccount = this.accountService.login(account.getUsername(), account.getPassword());

        } catch (ResourceNotFoundException e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loggedInAccount);
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(loggedInAccount);
    }

    @PostMapping("messages")
    public @ResponseBody ResponseEntity<Message> createMessage(@RequestBody Message newMessage)
    {
        boolean isBadRequest = true;
        if (!newMessage.getMessageText().isBlank() && newMessage.getMessageText().length() < 256)
        {
            Integer postedBy = newMessage.getPostedBy();
            
            try {
                Account accountPostedBy = this.accountService.findAccount(postedBy);
                this.messageService.saveMessage(newMessage);
                isBadRequest = false;
            } catch (Exception e) {
                isBadRequest = true;
                
            }
        }

        if (!isBadRequest)
            return ResponseEntity.status(HttpStatus.OK).body(newMessage);

        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @DeleteMapping("messages/{messageId}")
    public @ResponseBody ResponseEntity<Integer> deleteMessage(@PathVariable("messageId") Integer messageId)
    {
        boolean isMessageDeleted = false;
        try {
            Message foundMessage = this.messageService.findMessage(messageId);
            this.messageService.deleteMessage(messageId);
            isMessageDeleted = true;
        } catch (ResourceNotFoundException e) {
            e.getMessage();
        }
        
        if (isMessageDeleted)
            return ResponseEntity.status(HttpStatus.OK).body(1);
        else
            return ResponseEntity.status(HttpStatus.OK).body(null);

    }

    @GetMapping("messages/{messageId}")
    public @ResponseBody ResponseEntity<Message> getMessageById(@PathVariable("messageId") Integer messageId)
    {
        Message foundMessage = null;
        
        try {
            foundMessage = this.messageService.findMessage(messageId);
        } catch (ResourceNotFoundException e) {
            e.getMessage();
        }

        return ResponseEntity.status(HttpStatus.OK).body(foundMessage); 
    }
}
