package com.iridium.bankApi.mappers;

import com.iridium.bankApi.domain.Account;
import com.iridium.bankApi.domain.Person;
import com.iridium.bankApi.dtos.AccountDto;
import lombok.NonNull;

public class AccountMapper {
    public static AccountDto toAccountDto(@NonNull final Account account){
        return new AccountDto()
                .setNumber(account.getNumber())
                .setBalance(account.getBalance())
                .setHolderCpf(account.getPerson().getCpf())
                .setHolderName(account.getPerson().getName());
    }

    public static Account fromAccountDto(@NonNull final AccountDto accountDto){
        Person person = new Person(accountDto.getHolderCpf(), accountDto.getHolderName());

        return new Account(person, accountDto.getBalance());
    }
}
