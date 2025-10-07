package br.github.thiagohmm.cqrs_bank_project.event.model;

import br.github.thiagohmm.cqrs_bank_project.command.model.TransactionHistory;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TransactionCreatedEvent extends ApplicationEvent {
    private final TransactionHistory transaction;

    public TransactionCreatedEvent(Object source, TransactionHistory transaction) {
        super(source);
        this.transaction = transaction;
    }
}