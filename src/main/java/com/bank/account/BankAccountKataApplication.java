package com.bank.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.bank.account"})
public class BankAccountKataApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankAccountKataApplication.class, args);
    }
}
