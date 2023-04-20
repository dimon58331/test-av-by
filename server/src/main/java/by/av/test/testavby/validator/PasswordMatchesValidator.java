package by.av.test.testavby.validator;

import by.av.test.testavby.annotation.PasswordMatches;
import by.av.test.testavby.payload.request.RegistrationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        RegistrationRequest registrationRequest = (RegistrationRequest) value;
        return registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword());
    }
}
