package br.github.thiagohmm.cqrs_bank_project.command.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    private String id = UUID.randomUUID().toString();
    private String fullName;
    @Column(unique = true)
    private String document;
    @Column(unique = true)
    private String login;
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Account account;

    public User(String fullName, String document, String login, String password) {
        this.fullName = fullName;
        this.document = document;
        this.login = login;
        this.password = password;
    }
}