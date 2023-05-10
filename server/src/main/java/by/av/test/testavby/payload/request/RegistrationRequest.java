package by.av.test.testavby.payload.request;

import by.av.test.testavby.annotation.PasswordMatches;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@PasswordMatches
public class RegistrationRequest {
    @NotEmpty(message = "This field must be filled in")
    private String firstname;

    @NotEmpty(message = "This field must be filled in")
    private String lastname;

    private String patronymic;

    @NotEmpty(message = "This field must be filled in")
    @Email(regexp = "^\\S+@\\S+\\.\\S+$", message = "Email should be like 'test@test.test'")
    private String email;

    @NotEmpty(message = "This field must be filled in")
    @Pattern(regexp = "^\\+?[0-9]{9,15}$",
            message = "Phone number should be like '(+)375000000000, length 9-15 digits'")
    private String phoneNumber;

    @NotEmpty(message = "This field must be filled in")
    @Size(min = 6, message = "The size of password must be more than 6 characters")
    private String password;

    @NotEmpty(message = "The size of confirm password must be more than 6 characters")
    @Size(min = 6)
    private String confirmPassword;
}
