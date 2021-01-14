package com.bank.account.service;

import com.bank.account.exception.IllegalOperationException;
import com.bank.account.model.Account;
import com.bank.account.model.Client;
import com.bank.account.model.Operation;
import com.bank.account.repository.AccountRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.bank.account.repository.ClientRepository;
import com.bank.account.repository.OperationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OperationServiceIntegrationTest {

    private static String CLIENT_ID;
    private static String ACCOUNT_ID;

    @Autowired
    private OperationService operationService;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Before
    public void before() {
        Client client = Client.builder()
                    .firstname("Name 1")
                    .lastname("Lastname 1")
                    .build();
        CLIENT_ID = clientRepository.save(client).getId();

        Account account = Account.builder()
                .name("Account 1")
                .clientId(CLIENT_ID)
                .build();
        ACCOUNT_ID = accountRepository.save(account).getId();
    }

    @Test
    public void testDepositOperation() {
        // Given
        Operation operation = Operation.builder()
                .accountId(ACCOUNT_ID)
                .label("Op 1")
                .amount(3000)
                .build();

        // When
        operationService.saveOperation(operation);
        List<Operation> operations = operationService.findOperations(ACCOUNT_ID,
                Instant.now().minus(1, ChronoUnit.DAYS),
                Instant.now().plus(1, ChronoUnit.DAYS));

        // Then
        Account account = accountRepository.findOne(ACCOUNT_ID);
        assertThat(account.getAmount()).isEqualTo(operation.getAmount());
        assertThat(operations).containsOnly(operation);
    }

    @Test
    public void testWithdrawalOperation() {
        // Given
        Operation operation = Operation.builder()
                .accountId(ACCOUNT_ID)
                .label("Operation 1")
                .amount(-2000)
                .build();

        // When
        operationService.saveOperation(operation);
        List<Operation>
            operations =
            operationService.findOperations(ACCOUNT_ID,
                Instant.now().minus(1, ChronoUnit.DAYS),
                Instant.now().plus(1, ChronoUnit.DAYS));

        // Then
        Account account = accountRepository.findOne(ACCOUNT_ID);
        assertThat(account.getAmount()).isEqualTo(operation.getAmount());
        assertThat(operations).containsOnly(operation);
    }

    @Test
    public void testWithdrawalOperation_withUnallowedNegativeAmount() {
        // Given
        Account account = Account.builder()
                .id(ACCOUNT_ID)
                .name("Account 1")
                .allowNegativeAmount(false)
                .amount(23)
                .clientId(CLIENT_ID)
                .build();
        accountRepository.save(account);

        Operation operation = Operation.builder()
                .accountId(ACCOUNT_ID)
                .label("Operation 1")
                .amount(-2000)
                .build();

        // When / Then
        assertThatThrownBy(() -> operationService.saveOperation(operation))
            .isInstanceOf(IllegalOperationException.class).hasMessageContaining(ACCOUNT_ID);

        List<Operation>
            operations =
            operationService.findOperations(ACCOUNT_ID,
                Instant.now().minus(1, ChronoUnit.DAYS),
                Instant.now().plus(1, ChronoUnit.DAYS));

        assertThat(operations).isEmpty();
    }

    @Test
    public void testFindOperations() {
        // Given
        Operation todayOperation = Operation.builder()
                .accountId(ACCOUNT_ID)
                .label("Op 1")
                .amount(-1337)
                .build();
        Operation yesterdayOperation = Operation.builder()
                .accountId(ACCOUNT_ID)
                .date(Instant.now().minus(1, ChronoUnit.DAYS))
                .label("Op 2")
                .amount(3000)
                .build();
        Operation lastWeekOperation = Operation.builder()
                .accountId(ACCOUNT_ID)
                .date(Instant.now().minus(7, ChronoUnit.DAYS))
                .label("Op 3").amount(10).build();

        operationRepository.save(todayOperation);
        operationRepository.save(yesterdayOperation);
        operationRepository.save(lastWeekOperation);

        // When
        List<Operation> lastTwoDaysOperations = operationService
                .findOperations(ACCOUNT_ID, Instant.now().minus(2, ChronoUnit.DAYS), Instant.now());

        // Then
        assertThat(lastTwoDaysOperations).containsExactly(todayOperation, yesterdayOperation);
    }
}
