package com.bank.account.service;

import com.bank.account.model.Account;
import com.bank.account.repository.AccountRepository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.bank.account.util.ValidatorUtil.validate;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.Validate.notEmpty;

/**
 * Service for {@link Account}.
 */
@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = requireNonNull(accountRepository);
    }

    /**
     * Create a new account.
     * @param account to create
     * @return created {@link Account}
     */
    public Account createAccount(Account account) {
        return accountRepository.save(validate(account));
    }

    /**
     * Searchs accounts by client.
     * @param clientId as search criteria
     * @return list of found {@link Account}s
     */
    public List<Account> findAccountsByClient(String clientId) {
        return accountRepository.findAccountsByClientId(notEmpty(clientId));
    }
}
