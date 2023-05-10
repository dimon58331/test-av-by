package by.av.test.testavby.validator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResponseErrorValidation {
    public ResponseEntity<Object> mapValidationService(BindingResult result) {
        if (!result.hasErrors()) {
            return null;
        }
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError fieldError : result.getFieldErrors()) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        for (ObjectError objectError : result.getAllErrors()) {
            errorMap.put(objectError.getObjectName(), objectError.getDefaultMessage());
        }
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }
}
