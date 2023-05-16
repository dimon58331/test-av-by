package by.av.test.testavby.validator;

import by.av.test.testavby.annotation.ReleaseYearsMatches;
import by.av.test.testavby.dto.transport.GenerationTransportDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ReleaseYearsMatchesValidator implements ConstraintValidator<ReleaseYearsMatches, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        GenerationTransportDTO generationTransportDTO = (GenerationTransportDTO) value;
        return generationTransportDTO.getEndReleaseYear() >= generationTransportDTO.getStartReleaseYear();
    }
}
