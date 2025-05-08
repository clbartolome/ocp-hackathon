package com.hackathon.demoapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.demoapp.model.BankAccount;
import com.hackathon.demoapp.repository.BankAccountRepository;

@RestController
@RequestMapping("/accounts")
public class BankAccountController {

  private final BankAccountRepository repository;

  public BankAccountController(BankAccountRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public List<BankAccount> getAll() {
    return repository.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<BankAccount> getById(@PathVariable Long id) {
    Optional<BankAccount> account = repository.findById(id);
    return account.map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public BankAccount create(@RequestBody BankAccount account) {
    return repository.save(account);
  }

  @PutMapping("/{id}")
  public ResponseEntity<BankAccount> update(@PathVariable Long id, @RequestBody BankAccount updated) {
    return repository.findById(id)
        .map(acc -> {
          acc.setOwner(updated.getOwner());
          acc.setBalance(updated.getBalance());
          repository.save(acc);
          return ResponseEntity.ok(acc);
        })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return repository.findById(id)
        .map(acc -> {
          repository.delete(acc);
          return ResponseEntity.noContent().<Void>build();
        })
        .orElse(ResponseEntity.notFound().build());
  }
}