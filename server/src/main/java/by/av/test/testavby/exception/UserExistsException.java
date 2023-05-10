package by.av.test.testavby.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserExistsException extends RuntimeException {
    public UserExistsException(String message) {
        super(message);
    }
}
