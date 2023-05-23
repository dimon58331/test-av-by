package by.av.test.testavby.dto.transport;

import by.av.test.testavby.annotation.ReleaseYearsMatches;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@ReleaseYearsMatches
public class GenerationTransportDTO {
    private Long id;

    @NotEmpty(message = "Generation name cannot be empty")
    private String generationName;

    @NotNull(message = "Start release year cannot be null")
    @Min(value = 1900, message = "1990 is min")
    @Max(value = 2023, message = "2023 is max")
    private Integer startReleaseYear;

    @NotNull(message = "End release year cannot be null")
    @Min(value = 1900, message = "1990 is min")
    @Max(value = 2023, message = "2023 is max")
    private Integer endReleaseYear;

    private TransportModelDTO transportModel;
}
