package by.av.test.testavby.dto.transport;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TransportModelDTO {
    private Long id;

    @NotEmpty(message = "Model name cannot be empty")
    private String modelName;

    private TransportBrandDTO transportBrand;
}
