package com.iridium.bankApi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@Entity
public class Person implements Serializable {
    @Id
    private String cpf;

    @Column(nullable = false)
    private String name;

    public Person(final String cpf, final String name){
        this.cpf = cpf;
        this.name = name;
    }

    public void setName(final String name){
        this.name = name;
    }
}
