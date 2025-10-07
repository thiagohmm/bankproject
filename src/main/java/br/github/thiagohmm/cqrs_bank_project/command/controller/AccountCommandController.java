package br.github.thiagohmm.cqrs_bank_project.command.controller;

import br.github.thiagohmm.cqrs_bank_project.command.dto.TransactionRequest;
import br.github.thiagohmm.cqrs_bank_project.command.service.AccountCommandService;
import br.github.thiagohmm.cqrs_bank_project.security.service.AuthenticatedUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountCommandController {

    private final AccountCommandService accountCommandService;
    private final AuthenticatedUserService authenticatedUserService;

    @PostMapping("/deposit")
    public ResponseEntity<Void> deposit(@Valid @RequestBody TransactionRequest request) {
        String userId = authenticatedUserService.getAuthenticatedUserId();
        accountCommandService.deposit(userId, request.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pay")
    public ResponseEntity<Void> payBill(@Valid @RequestBody TransactionRequest request) {
        String userId = authenticatedUserService.getAuthenticatedUserId();
        accountCommandService.payBill(userId, request.getAmount());
        return ResponseEntity.ok().build();
    }
}