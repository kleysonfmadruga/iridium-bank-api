package com.iridium.bankApi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@Entity
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer number;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "holder_cpf", referencedColumnName = "cpf")
    private Person person;

    @Column
    private Double balance;

    public Account(final Person person, final Double balance){
        this.person = person;
        this.balance = balance;
    }

    public void setPerson(final Person person) {
        this.person = person;
    }

    public void withdraw(final Double value) throws RuntimeException {
        if (value > balance) throw new RuntimeException("Value to withdraw can't be greater than the available balance");
        else if (value <= 0) throw new RuntimeException("Value to withdraw can't be lower than or equals to zero");
        this.balance -= value;
    }

    public void deposit(final Double value) throws RuntimeException {
        if (value <= 0) throw new RuntimeException("Value to deposit can't be lower than or equals to zero");
        this.balance += value;
    }
}
