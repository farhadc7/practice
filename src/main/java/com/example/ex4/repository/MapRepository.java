package com.example.ex4.repository;

import com.example.ex4.enitty.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MapRepository {
    Long save(BigDecimal initialBalance);

    Optional<Account> getById(Long id);

    List<Account> findAll();
}
