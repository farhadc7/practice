package com.example.ex4.repository;

import com.example.ex4.enitty.Account;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Singleton
public class AccountRepository extends AbstractMapRepository{

    private AtomicLong accountId=new AtomicLong(0);

    @Override
    public Long save(BigDecimal initialBalance) {
        Account ac= new Account(accountId.incrementAndGet(),initialBalance);
        accountRepo.putIfAbsent(ac.getId(),ac);
        return ac.getId();
    }
}
