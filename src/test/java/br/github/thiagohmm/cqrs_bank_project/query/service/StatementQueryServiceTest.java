
package br.github.thiagohmm.cqrs_bank_project.query.service;

import br.github.thiagohmm.cqrs_bank_project.query.model.UserStatement;
import br.github.thiagohmm.cqrs_bank_project.query.repository.UserStatementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class StatementQueryServiceTest {

    @Mock
    private UserStatementRepository userStatementRepository;

    @InjectMocks
    private StatementQueryService statementQueryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetStatement_Success() {
        UserStatement statement = new UserStatement("1");

        when(userStatementRepository.findById("1")).thenReturn(Optional.of(statement));

        UserStatement result = statementQueryService.getStatement("1");

        assertNotNull(result);
        assertEquals("1", result.getUserId());
    }

    @Test
    public void testGetStatement_NotFound() {
        when(userStatementRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            statementQueryService.getStatement("1");
        });
    }
}
