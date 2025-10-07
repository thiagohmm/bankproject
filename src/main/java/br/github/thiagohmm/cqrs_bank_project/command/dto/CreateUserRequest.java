package br.github.thiagohmm.cqrs_bank_project.command.dto;

import br.github.thiagohmm.cqrs_bank_project.security.validator.ValidCPF;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserRequest {
    @NotBlank(message = "Nome completo é obrigatório")
    private String fullName;

    @NotBlank(message = "Documento é obrigatório")
    @ValidCPF
    private String document;

    @NotBlank(message = "Login é obrigatório")
    private String login;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String password;
}