package br.github.thiagohmm.cqrs_bank_project.event.listener;

import br.github.thiagohmm.cqrs_bank_project.command.model.Account;
import br.github.thiagohmm.cqrs_bank_project.command.model.TransactionHistory;
import br.github.thiagohmm.cqrs_bank_project.command.model.TransactionType;
import br.github.thiagohmm.cqrs_bank_project.command.repository.AccountRepository;
import br.github.thiagohmm.cqrs_bank_project.event.model.TransactionCreatedEvent;
import br.github.thiagohmm.cqrs_bank_project.query.model.UserStatement;
import br.github.thiagohmm.cqrs_bank_project.query.repository.UserStatementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionEventListener {

    private final UserStatementRepository statementRepository;
    private final AccountRepository accountRepository;

    @Async
    @EventListener
    public void handleTransactionEvent(TransactionCreatedEvent event) {
        log.info("Evento de transação recebido. Atualizando projeção de leitura...");

        TransactionHistory tx = event.getTransaction();
        String userId = tx.getAccount().getUser().getId();

        UserStatement statement = statementRepository.findById(userId)
                .orElse(new UserStatement(userId));

        // Busca o saldo mais recente do banco SQL (fonte da verdade)
        BigDecimal currentBalance = accountRepository.findById(tx.getAccount().getId())
                .map(Account::getBalance)
                .orElse(BigDecimal.ZERO);

        statement.setTotalBalance(currentBalance);

        UserStatement.HistoryItem historyItem = new UserStatement.HistoryItem(
                tx.getType() == TransactionType.DEPOSIT ? "deposito" : "saque",
                tx.getTransactionValue(),
                tx.getTimestamp()
        );
        // Adiciona no início da lista para manter a ordem cronológica decrescente
        statement.getHistory().add(0, historyItem);

        statementRepository.save(statement);
        log.info("Projeção de leitura para o usuário {} atualizada com sucesso.", userId);
    }
}