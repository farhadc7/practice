package com.example.ex4.repository;

import com.example.ex4.enitty.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class AccountRepositoryTest {
    public AccountRepository accountRepository;
    public AccountRepositoryNotSafe accountRepositoryNotSafe;

    BigDecimal initialBalance;
    
    @BeforeEach
    public void setup(){
        accountRepository= new AccountRepository();
        accountRepositoryNotSafe= new AccountRepositoryNotSafe();
         initialBalance= BigDecimal.valueOf(5000);
    }
    
    
    @Test
    public void testSaveSuccessfully(){
        
        Long id = accountRepository.save(initialBalance);
        
        Account account =accountRepository.getById(id).orElse(null);
        
        assertNotNull(account);
        assertEquals(initialBalance,account.getBalance());
        
    }
    @Test
    public void saveMultipleAccountsConcurrently(){
        int testSize= 100;

        List<CompletableFuture> threadList= new ArrayList<>();
        Supplier supplier= ()->{
            return accountRepository.save(initialBalance);
        };

        IntStream.range(0,testSize).parallel().forEach(i-> threadList.add(CompletableFuture.supplyAsync(supplier)));
        threadList.parallelStream().forEach(CompletableFuture::join);

        List<Account> accountList = accountRepository.findAll();

        assertEquals(testSize,accountList.size());

    }

    @Test
    public void saveMultipleAccountsNotConcurrently(){
        int testSize= 100;

        List<CompletableFuture> threadList= new ArrayList<>();
        Supplier supplier= ()->{
            return accountRepositoryNotSafe.save(initialBalance);
        };

        IntStream.range(0,testSize).parallel().forEach(i-> threadList.add(CompletableFuture.supplyAsync(supplier)));
        threadList.parallelStream().forEach(CompletableFuture::join);

        List<Account> accountList = accountRepositoryNotSafe.findAll();

        assertEquals(testSize,accountList.size());

    }

}