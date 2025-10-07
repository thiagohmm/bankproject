
package br.github.thiagohmm.cqrs_bank_project.event.listener;

import br.github.thiagohmm.cqrs_bank_project.command.model.Account;
import br.github.thiagohmm.cqrs_bank_project.command.model.TransactionHistory;
import br.github.thiagohmm.cqrs_bank_project.command.model.TransactionType;
import br.github.thiagohmm.cqrs_bank_project.command.model.User;
import br.github.thiagohmm.cqrs_bank_project.command.repository.AccountRepository;
import br.github.thiagohmm.cqrs_bank_project.event.model.TransactionCreatedEvent;
import br.github.thiagohmm.cqrs_bank_project.query.model.UserStatement;
import br.github.thiagohmm.cqrs_bank_project.query.repository.UserStatementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class TransactionEventListenerTest {

    @Mock
    private UserStatementRepository statementRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionEventListener eventListener;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleTransactionEvent() {
        User user = new User("Test User", "12345678901", "testuser", "password");
        user.setId("1");
        Account account = new Account(user);
        account.setId("1");
        account.setBalance(new BigDecimal("100.00"));

        TransactionHistory transaction = new TransactionHistory();
        transaction.setAccount(account);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setTransactionValue(new BigDecimal("50.00"));
        transaction.setTimestamp(LocalDateTime.now());

        TransactionCreatedEvent event = new TransactionCreatedEvent(this, transaction);

        UserStatement statement = new UserStatement("1");

        when(statementRepository.findById("1")).thenReturn(Optional.of(statement));
        when(accountRepository.findById("1")).thenReturn(Optional.of(account));

        eventListener.handleTransactionEvent(event);

        verify(statementRepository).save(statement);
    }
}
