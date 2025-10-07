package br.github.thiagohmm.cqrs_bank_project.command.service;
import br.github.thiagohmm.cqrs_bank_project.command.model.Account;
import br.github.thiagohmm.cqrs_bank_project.command.model.TransactionHistory;
import br.github.thiagohmm.cqrs_bank_project.command.model.TransactionType;
import br.github.thiagohmm.cqrs_bank_project.command.repository.AccountRepository;
import br.github.thiagohmm.cqrs_bank_project.event.model.TransactionCreatedEvent;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountCommandService {

    private final AccountRepository accountRepository;
    private final ApplicationEventPublisher eventPublisher;
    private static final BigDecimal OVERDRAFT_INTEREST_RATE = new BigDecimal("0.02");

    @Transactional
    public void deposit(String userId, BigDecimal amount) {
        Account account = findAccountByUserId(userId);
        BigDecimal currentBalance = account.getBalance();

        // Regra: Se a conta estava negativa, quita a dívida com juros antes de depositar o restante
        if (currentBalance.compareTo(BigDecimal.ZERO) < 0) {
            BigDecimal debt = currentBalance.abs();
            BigDecimal interest = debt.multiply(OVERDRAFT_INTEREST_RATE);
            currentBalance = currentBalance.subtract(interest); // Apply interest to the debt
        }

        account.setBalance(currentBalance.add(amount));

        TransactionHistory tx = createAndSaveTransaction(account, amount, TransactionType.DEPOSIT);
        eventPublisher.publishEvent(new TransactionCreatedEvent(this, tx));
    }

    @Transactional
    public void payBill(String userId, BigDecimal amount) {
        Account account = findAccountByUserId(userId);
        account.setBalance(account.getBalance().subtract(amount)); // Permite ficar negativo

        TransactionHistory tx = createAndSaveTransaction(account, amount, TransactionType.WITHDRAWAL);
        eventPublisher.publishEvent(new TransactionCreatedEvent(this, tx));
    }

    private Account findAccountByUserId(String userId) {
        return accountRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada para o usuário " + userId));
    }

    private TransactionHistory createAndSaveTransaction(Account account, BigDecimal value, TransactionType type) {
        TransactionHistory tx = new TransactionHistory();
        tx.setAccount(account);
        tx.setTransactionValue(value);
        tx.setType(type);
        tx.setTimestamp(LocalDateTime.now());
        account.getHistory().add(tx);
        accountRepository.save(account); // Salva a conta para persistir a transação
        return tx;
    }
}