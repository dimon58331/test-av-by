package by.av.test.testavby.dto.transport;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TransportTypeDTO {
    @NotEmpty(message = "Type name cannot be empty")
    private String typeName;
}
