package com.bank.account.service;

import com.bank.account.exception.IllegalOperationException;
import com.bank.account.model.Account;
import com.bank.account.model.Operation;
import com.bank.account.repository.AccountRepository;
import com.bank.account.repository.OperationRepository;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OperationServiceUnitTest {

    @Mock
    private AccountRepository accountRepositoryMock;

    @Mock
    private OperationRepository operationRepositoryMock;

    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    private OperationService operationService;

    @Before
    public void before() {
        initMocks(this);
        operationService = new OperationService(accountRepositoryMock, operationRepositoryMock);
    }

    @Test
    public void testSaveDepositOperation() {
        // Given
        Account account = Account.builder()
                .id(UUID.randomUUID().toString())
                .name("Account 1")
                .amount(23)
                .build();

        Operation operation = Operation.builder()
                .accountId(account.getId())
                .label("Op 1")
                .amount(24)
                .build();

        when(accountRepositoryMock.findOne(account.getId())).thenReturn(account);

        // When
        operationService.saveOperation(operation);

        // Then
        verify(operationRepositoryMock).save(operation);
        verify(accountRepositoryMock).save(accountCaptor.capture());

        Account accountCaptured = accountCaptor.getValue();
        assertThat(accountCaptured.getId()).isEqualTo(account.getId());
        assertThat(accountCaptured.getAmount()).isEqualTo(47);
    }

    @Test
    public void testSaveWithdrawalOperation() {
        // Given
        Account account = Account.builder()
                .id(UUID.randomUUID().toString())
                .name("Op 1")
                .amount(45)
                .build();

        Operation operation = Operation.builder()
                .accountId(account.getId())
                .label("Op 2")
                .amount(-200)
                .build();

        when(accountRepositoryMock.findOne(account.getId())).thenReturn(account);

        // When
        operationService.saveOperation(operation);

        // Then
        verify(operationRepositoryMock).save(operation);
        verify(accountRepositoryMock).save(accountCaptor.capture());

        Account accountCaptured = accountCaptor.getValue();
        assertThat(accountCaptured.getId()).isEqualTo(account.getId());
        assertThat(accountCaptured.getAmount()).isEqualTo(-155);
    }

    @Test
    public void testSaveWithdrawalOperation_withNegativeAmountUnallowed() {
        // Given
        Account account = Account.builder()
                .id(UUID.randomUUID().toString())
                .allowNegativeAmount(false)
                .name("Account 1")
                .amount(344).build();

        Operation operation = Operation.builder()
                .accountId(account.getId())
                .label("Op 1")
                .amount(-400)
                .build();

        when(accountRepositoryMock.findOne(account.getId())).thenReturn(account);

        // Then
        assertThatThrownBy(() -> operationService.saveOperation(operation))
            .isInstanceOf(IllegalOperationException.class);

        verify(operationRepositoryMock, never()).save(operation);
        verify(accountRepositoryMock, never()).save(accountCaptor.capture());
    }

}
