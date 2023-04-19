package by.av.test.testavby.payload.response;

import lombok.Data;
import lombok.Getter;

@Getter
public class InvalidLoginResponse {
    private String email = "Invalid email";
    private String password = "Invalid password";
}
