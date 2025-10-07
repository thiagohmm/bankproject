package br.github.thiagohmm.cqrs_bank_project.query.repository;

import br.github.thiagohmm.cqrs_bank_project.query.model.UserStatement;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserStatementRepository extends MongoRepository<UserStatement, String> {
}