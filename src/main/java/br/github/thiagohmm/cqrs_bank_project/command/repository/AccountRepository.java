package br.github.thiagohmm.cqrs_bank_project.command.repository;


import br.github.thiagohmm.cqrs_bank_project.command.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByUserId(String userId);
}