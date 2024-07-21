package com.example.ex4.service;

import com.example.ex4.enitty.Account;
import com.example.ex4.exception.BussinessException;
import com.example.ex4.repository.AbstractMapRepository;
import com.example.ex4.repository.AccountRepository;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Qualifier;
import javax.inject.Singleton;
import java.math.BigDecimal;

@Singleton
@Log
public class AccountService {
    private final AbstractMapRepository repository;
    
    
    @Inject
    public AccountService( AccountRepository repository) {
        this.repository = repository;
    }
    
    public void transfer(Long senderId, Long receiverId, BigDecimal amount){
        withdraw(getById(senderId),amount);
        deposit(getById(receiverId),amount);
    }

    private void deposit(Account account, BigDecimal amount) {
        synchronized (account){
        log.info(Thread.currentThread().getName()+" deposit from account:"+account.getId());
        sleep(100);
            account.setBalance(account.getBalance().add(amount));
        }
    }

    private void withdraw(Account account, BigDecimal amount) {
        checkBalance(account,amount);

        synchronized (account){
            sleep(100);
            log.info(Thread.currentThread().getName()+" withdraw from account:"+account.getId());
            checkBalance(account,amount);
            account.setBalance(account.getBalance().subtract(amount));
        }
    }

    private void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Account getById(Long id){
        return repository.getById(id).orElseThrow(()->new BussinessException("account not found for id: "+id));
    }

    private void checkBalance(Account account, BigDecimal amount) {
        if(account.getBalance().compareTo(amount)<0){
            throw new BussinessException("balance is not enough for transfer from account:"+account.getId());
        }
    }


}
