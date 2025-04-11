package com.example.controller;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

        if (!newAccount.getUsername().isBlank() && newAccount.getPassword().length() > 3)
        {
            try {
                Account searchAccount = this.accountService.findAccount(newAccount.getAccountId());
                if (searchAccount.getUsername() == newAccount.getUsername())
                {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(newAccount);
                }

                else
                {
                    this.accountService.registerAccount(newAccount);
                    return ResponseEntity.status(HttpStatus.CREATED).body(newAccount);
                }
            } catch (ResourceNotFoundException e) {
                e.getMessage();
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        
    }

}
