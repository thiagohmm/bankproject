package br.github.thiagohmm.cqrs_bank_project.query.service;

import br.github.thiagohmm.cqrs_bank_project.query.model.UserStatement;
import br.github.thiagohmm.cqrs_bank_project.query.repository.UserStatementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatementQueryService {

    private final UserStatementRepository statementRepository;

    public UserStatement getStatement(String userId) {
        return statementRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Extrato não encontrado para o usuário. Nenhuma transação foi realizada ainda."));
    }
}