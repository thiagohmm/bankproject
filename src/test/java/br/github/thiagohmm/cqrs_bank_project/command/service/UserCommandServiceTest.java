
package br.github.thiagohmm.cqrs_bank_project.command.service;

import br.github.thiagohmm.cqrs_bank_project.command.dto.CreateUserRequest;
import br.github.thiagohmm.cqrs_bank_project.command.model.User;
import br.github.thiagohmm.cqrs_bank_project.command.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserCommandServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserCommandService userCommandService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser_Success() {
        CreateUserRequest request = new CreateUserRequest("Test User", "12345678901", "testuser", "password");

        when(userRepository.existsByLoginOrDocument(request.getLogin(), request.getDocument())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId("1");
            return user;
        });

        User createdUser = userCommandService.createUser(request);

        assertNotNull(createdUser);
        assertEquals("Test User", createdUser.getFullName());
        assertEquals("12345678901", createdUser.getDocument());
        assertEquals("testuser", createdUser.getLogin());
        assertEquals("encodedPassword", createdUser.getPassword());
        assertNotNull(createdUser.getAccount());
        assertEquals(createdUser, createdUser.getAccount().getUser());
    }

    @Test
    public void testCreateUser_UserAlreadyExists() {
        CreateUserRequest request = new CreateUserRequest("Test User", "12345678901", "testuser", "password");

        when(userRepository.existsByLoginOrDocument(request.getLogin(), request.getDocument())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            userCommandService.createUser(request);
        });
    }
}
