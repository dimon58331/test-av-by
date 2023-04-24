package by.av.test.testavby.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserDTO {
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
}
