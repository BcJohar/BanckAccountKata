package com.bank.account.service;

import com.bank.account.model.Account;
import com.bank.account.model.Client;
import com.bank.account.repository.ClientRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountServiceIntegrationTest {

    private static final String CLIENT_ID = UUID.randomUUID().toString();

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientRepository clientRepository;

    @Before
    public void before() {
        Client client = Client.builder()
                .id(CLIENT_ID)
                .firstname("Client 1 name")
                .lastname("Client 2 lastname")
                .build();
        clientRepository.save(client);
    }

    @Test
    public void testCreateAndFind() {
        // Given
        Account account1 = Account.builder()
                .clientId(CLIENT_ID)
                .name("Account 1")
                .build();
        Account account2 = Account.builder()
                .clientId(CLIENT_ID)
                .name("Account 2")
                .build();

        // When
        Account accountCreated1 = accountService.createAccount(account1);
        Account accountCreated2 = accountService.createAccount(account2);
        List<Account> accounts = accountService.findAccountsByClient(CLIENT_ID);

        // Then
        assertAccount(accountCreated1, account1);
        assertAccount(accountCreated2, account2);
        assertThat(accounts).containsExactlyInAnyOrder(accountCreated1, accountCreated2);
    }

    private void assertAccount(Account actual, Account expected) {
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getClientId()).isEqualTo(expected.getClientId());
        assertThat(actual.getAmount()).isEqualTo(expected.getAmount());
        assertThat(actual.isAllowNegativeAmount()).isEqualTo(expected.isAllowNegativeAmount());
        assertThat(actual.getCurrency()).isEqualTo(expected.getCurrency());
    }

}
