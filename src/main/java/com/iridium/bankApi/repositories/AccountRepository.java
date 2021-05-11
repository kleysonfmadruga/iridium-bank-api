package com.iridium.bankApi.repositories;

import com.iridium.bankApi.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
}
