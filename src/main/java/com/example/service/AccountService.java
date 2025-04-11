package com.example.service;

import com.example.repository.AccountRepository;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.ResourceNotFoundException;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository)
    {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAccountList()
    {
        return (List<Account>) this.accountRepository.findAll();
    }

    public void registerAccount(Account account)
    {
        this.accountRepository.save(account);
    }

    public Account findAccount(Integer accountId) throws ResourceNotFoundException
    {
        return this.accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Message with ID: " + accountId + " not found"));
    }

    public boolean doesUsernameExist(String username)
    {
        return this.accountRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Account with username: " + username + " not found")) != null;
    }

}
