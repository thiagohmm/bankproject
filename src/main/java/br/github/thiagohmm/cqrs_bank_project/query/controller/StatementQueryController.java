package br.github.thiagohmm.cqrs_bank_project.query.controller;

import br.github.thiagohmm.cqrs_bank_project.query.model.UserStatement;
import br.github.thiagohmm.cqrs_bank_project.query.service.StatementQueryService;
import br.github.thiagohmm.cqrs_bank_project.security.service.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statement")
@RequiredArgsConstructor
public class StatementQueryController {

    private final StatementQueryService queryService;
    private final AuthenticatedUserService authenticatedUserService;

    @GetMapping
    public ResponseEntity<UserStatement> getMyStatement() {
        String userId = authenticatedUserService.getAuthenticatedUserId();
        return ResponseEntity.ok(queryService.getStatement(userId));
    }
}