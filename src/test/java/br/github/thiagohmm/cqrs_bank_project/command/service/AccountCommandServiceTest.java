package br.github.thiagohmm.cqrs_bank_project.command.service;

import br.github.thiagohmm.cqrs_bank_project.command.model.Account;
import br.github.thiagohmm.cqrs_bank_project.command.model.User;
import br.github.thiagohmm.cqrs_bank_project.command.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountCommandServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AccountCommandService accountCommandService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeposit_PositiveBalance() {
        User user = new User("Test User", "12345678901", "testuser", "password");
        Account account = new Account(user);
        account.setBalance(new BigDecimal("100.00"));

        when(accountRepository.findByUserId("1")).thenReturn(Optional.of(account));

        accountCommandService.deposit("1", new BigDecimal("50.00"));

        assertEquals(0, new BigDecimal("150.00").compareTo(account.getBalance()));
        verify(accountRepository).save(any(Account.class));
        verify(eventPublisher).publishEvent(any());
    }

    @Test
    public void testDeposit_NegativeBalance() {
        User user = new User("Test User", "12345678901", "testuser", "password");
        Account account = new Account(user);
        account.setBalance(new BigDecimal("-50.00"));

        when(accountRepository.findByUserId("1")).thenReturn(Optional.of(account));

        accountCommandService.deposit("1", new BigDecimal("100.00"));

        assertEquals(0, new BigDecimal("49.00").compareTo(account.getBalance()));
        verify(accountRepository).save(any(Account.class));
        verify(eventPublisher).publishEvent(any());
    }

    @Test
    public void testPayBill() {
        User user = new User("Test User", "12345678901", "testuser", "password");
        Account account = new Account(user);
        account.setBalance(new BigDecimal("100.00"));

        when(accountRepository.findByUserId("1")).thenReturn(Optional.of(account));

        accountCommandService.payBill("1", new BigDecimal("150.00"));

        assertEquals(0, new BigDecimal("-50.00").compareTo(account.getBalance()));
        verify(accountRepository).save(any(Account.class));
        verify(eventPublisher).publishEvent(any());
    }
}