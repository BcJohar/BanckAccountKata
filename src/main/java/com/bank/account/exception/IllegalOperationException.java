package com.bank.account.exception;

import com.bank.account.model.Account;
import com.bank.account.model.Operation;

public class IllegalOperationException extends RuntimeException {

    private static final String MESSAGE = "Illegal operation %s on account %s";

    public IllegalOperationException(Account account, Operation operation) {
        super(String.format(MESSAGE, operation, account.getId()));
    }

}
