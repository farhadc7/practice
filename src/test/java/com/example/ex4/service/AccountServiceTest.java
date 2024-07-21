package com.example.ex4.service;

import com.example.ex4.enitty.Account;
import com.example.ex4.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    private AccountService accountService;
    private AccountRepository accountRepository;

    private long ac1;
    private long ac2;
    private BigDecimal value;

    @BeforeEach
    public void setup(){
        accountRepository= new AccountRepository();
        accountService= new AccountService(accountRepository);
         ac1= accountRepository.save(BigDecimal.valueOf(5000));
         ac2= accountRepository.save(BigDecimal.valueOf(5000));
        value= BigDecimal.valueOf(1000);
    }

    @Test
    public void transferBetweenTwoAccountsSuccessfully(){
        var currentBalance1= accountService.getById(ac1).getBalance();
        var currentBalance2= accountService.getById(ac2).getBalance();
        accountService.transfer(ac1,ac2,value);

        var account1= accountService.getById(ac1);
        var account2= accountService.getById(ac2);

        assertEquals(currentBalance1.subtract(value),account1.getBalance());
        assertEquals(currentBalance2.add(value),account2.getBalance());
    }

    @Test
    public void transferBetweenTwoAccountsManyTimesConcurrently(){
        BigDecimal initialAccount1= accountService.getById(ac1).getBalance();
        BigDecimal initialAccount2= accountService.getById(ac2).getBalance();

        List<CompletableFuture> threadList= new ArrayList<>();

        BigDecimal amount=BigDecimal.valueOf(1);
        int testSize= 100;

        Supplier s= ()->{
            accountService.transfer(ac1,ac2,amount);
            return null;
        };

        IntStream.range(0,testSize).forEach(i-> threadList.add(CompletableFuture.supplyAsync(s)));

        threadList.parallelStream().forEach(CompletableFuture::join);

        Account account1= accountService.getById(ac1);
        Account account2= accountService.getById(ac2);

        assertEquals(initialAccount1.subtract(BigDecimal.valueOf(testSize)),account1.getBalance());
        assertEquals(initialAccount2.add(BigDecimal.valueOf(testSize)),account2.getBalance());

    }

}