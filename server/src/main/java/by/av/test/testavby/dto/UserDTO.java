package by.av.test.testavby.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    @NotEmpty(message = "This field must be filled in")
    private String firstName;
    @NotEmpty(message = "This field must be filled in")
    private String lastName;
    private String patronymic;
    @NotEmpty(message = "This field must be filled in")
    @Email(regexp = "^\\S+@\\S+\\.\\S+$", message = "Email should be like 'test@test.test'")
    private String email;
    @NotEmpty(message = "This field must be filled in")
    @Pattern(regexp = "^\\+?[1-9][0-9]{12,13}$",
            message = "Phone number should be like '(+)375000000000, length 12-13 digits'")
    private String phoneNumber;
    @NotEmpty(message = "This field must be filled in")
    @Size(min = 6, message = "The size of password must be more than 6 characters")
    private String password;
}
