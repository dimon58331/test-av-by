package by.av.test.testavby.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ImageModelNotFoundException extends RuntimeException{
    public ImageModelNotFoundException(String message) {
        super(message);
    }
}
