package br.github.thiagohmm.cqrs_bank_project.security.validator;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<ValidCPF, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // Deixa para @NotBlank cuidar disso
        }
        try {
            CPFValidator cpfValidator = new CPFValidator();
            cpfValidator.assertValid(value);
            return true;
        } catch (InvalidStateException e) {
            return false;
        }
    }
}