package br.github.thiagohmm.cqrs_bank_project.command.repository;

import br.github.thiagohmm.cqrs_bank_project.command.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByLogin(String login);
    boolean existsByLoginOrDocument(String login, String document);
}