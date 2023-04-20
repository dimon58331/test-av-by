package by.av.test.testavby.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthenticationRequest {
    @NotEmpty(message = "This field must be filled in")
    @Email(regexp = "^\\S+@\\S+\\.\\S+$", message = "Email should be like 'test@test.test'")
    private String email;
    @NotEmpty(message = "This field must be filled in")
    @Size(min = 6, message = "The size of password must be more than 6 characters")
    private String password;
}
