package com.bank.account.model;

import java.util.Currency;
import java.util.Locale;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Accounts entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String clientId;

    private double amount;

    @Builder.Default
    private boolean allowNegativeAmount = true;

    @NotNull
    @Builder.Default
    private Currency currency = Currency.getInstance(Locale.getDefault());

}
