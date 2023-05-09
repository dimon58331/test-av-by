package by.av.test.testavby.dto.transport;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class GenerationTransportDTO {

    @NotEmpty(message = "Generation name cannot be empty")
    private String generationName;

    @NotEmpty(message = "Start release year cannot be empty")
    private Integer startReleaseYear;

    @NotEmpty(message = "End release year cannot be empty")
    private Integer endReleaseYear;

    private TransportModelDTO transportModel;
}
