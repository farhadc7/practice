package com.example.ex4.repository;

import com.example.ex4.enitty.Account;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class AccountRepositoryNotSafe extends AbstractMapRepository{

    private Long accountIdNotSafe= 0l;


    @Override
    public Long save(BigDecimal initialBalance){

        Account ac= new Account(accountIdNotSafe++,initialBalance);
        accountRepo.putIfAbsent(ac.getId(),ac);
        return ac.getId();
    }

}
