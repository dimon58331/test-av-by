package by.av.test.testavby.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TransportExistsException extends RuntimeException {
    public TransportExistsException(String message) {
        super(message);
    }
}
