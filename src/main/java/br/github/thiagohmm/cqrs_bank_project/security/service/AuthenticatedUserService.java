package br.github.thiagohmm.cqrs_bank_project.security.service;

import br.github.thiagohmm.cqrs_bank_project.command.model.User;
import br.github.thiagohmm.cqrs_bank_project.command.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticatedUserService {

    private final UserRepository userRepository;

    public String getAuthenticatedUserId() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByLogin(login)
                .map(User::getId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário autenticado não encontrado no banco de dados."));
    }
}