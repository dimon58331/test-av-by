package by.av.test.testavby.dto.transport;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TransportBrandDTO {
    @NotEmpty(message = "Brand name cannot be empty")
    private String brandName;
}
