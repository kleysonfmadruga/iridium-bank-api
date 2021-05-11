package com.iridium.bankApi.services;

import com.iridium.bankApi.domain.Account;
import com.iridium.bankApi.dtos.AccountDto;
import com.iridium.bankApi.repositories.AccountRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.iridium.bankApi.mappers.AccountMapper.*;

@Service
public class AccountService {
    @Autowired
    private AccountRepository repository;

    public AccountDto findAccount(Integer number) throws RuntimeException {
        var queryResult = repository.findById(number);

        if (queryResult.isEmpty()) throw new RuntimeException("No accounts were found with this number");

        var account = queryResult.get();

        return toAccountDto(account);
    }

    public AccountDto createAccount(AccountDto accountDto) throws RuntimeException {
        Account savedAccount;
        try {
            savedAccount = repository.save( fromAccountDto(accountDto) );
        } catch (RuntimeException e){
            throw new RuntimeException("This CPF is already registered in this bank");
        }

        return toAccountDto(savedAccount);
    }

    public AccountDto deposit(Integer accountNumber, Double depositValue) throws RuntimeException {
        var queryResult = repository.findById(accountNumber);

        if (queryResult.isEmpty()) throw new RuntimeException("No accounts were found with this number");

        var account = queryResult.get();
        account.deposit(depositValue);

        var savedAccount = repository.save(account);
        return toAccountDto(savedAccount);
    }

    public AccountDto withdraw(Integer accountNumber, Double withdrawValue) throws RuntimeException {
        var queryResult = repository.findById(accountNumber);

        if (queryResult.isEmpty()) throw new RuntimeException("No accounts were found with this number");

        var account = queryResult.get();
        account.withdraw(withdrawValue);

        var savedAccount = repository.save(account);
        return toAccountDto(savedAccount);
    }

    public AccountDto transference(Integer fromAccountNumber, Integer toAccountNumber, Double transferenceValue) throws RuntimeException {
        if (fromAccountNumber.equals(toAccountNumber)) throw new RuntimeException("The transferring account number can't be equals to the transfer destination account number");

        var fromAccountQueryResult = repository.findById(fromAccountNumber);
        var toAccountQueryResult = repository.findById(toAccountNumber);

        if (fromAccountQueryResult.isEmpty()) throw new RuntimeException("The transferring account was not found with this number");
        if (toAccountQueryResult.isEmpty()) throw new RuntimeException("The transfer destination account was not found with this number");

        var fromAccount = fromAccountQueryResult.get();
        var toAccount = toAccountQueryResult.get();

        fromAccount.withdraw(transferenceValue);
        toAccount.deposit(transferenceValue);

        var savedAccount = repository.save(fromAccount);
        repository.save(toAccount);

        return toAccountDto(savedAccount);
    }

    public void deleteAccount(Integer accountNumber) throws RuntimeException {
        var queryResult = repository.findById(accountNumber);

        if (queryResult.isEmpty()) throw new RuntimeException("No accounts were found with this number");

        var account = queryResult.get();

        repository.delete(account);
    }
}
