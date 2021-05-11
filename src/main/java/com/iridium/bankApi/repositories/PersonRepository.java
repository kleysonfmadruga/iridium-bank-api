package com.iridium.bankApi.repositories;

import com.iridium.bankApi.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {
}
