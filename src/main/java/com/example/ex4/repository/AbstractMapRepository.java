package com.example.ex4.repository;

import com.example.ex4.enitty.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractMapRepository implements MapRepository{
    protected Map<Long, Account> accountRepo= new ConcurrentHashMap<>();

    @Override
    public abstract Long save(BigDecimal initialBalance);

    @Override
    public Optional<Account> getById(Long id) {
        return Optional.ofNullable(accountRepo.get(id));
    }

    @Override
    public List<Account> findAll() {
        return accountRepo.values().stream().toList();
    }
}
