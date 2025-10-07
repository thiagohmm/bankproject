package br.github.thiagohmm.cqrs_bank_project.command.service;

import br.github.thiagohmm.cqrs_bank_project.command.dto.CreateUserRequest;
import br.github.thiagohmm.cqrs_bank_project.command.model.Account;
import br.github.thiagohmm.cqrs_bank_project.command.model.User;
import br.github.thiagohmm.cqrs_bank_project.command.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public User createUser(CreateUserRequest request) {
        if (userRepository.existsByLoginOrDocument(request.getLogin(), request.getDocument())) {
            throw new IllegalArgumentException("Usuário com este login ou documento já existe.");
        }

        User user = new User(
                request.getFullName(),
                request.getDocument(),
                request.getLogin(),
                passwordEncoder.encode(request.getPassword())
        );

        Account account = new Account(user);
        user.setAccount(account);

        return userRepository.save(user);
    }
}