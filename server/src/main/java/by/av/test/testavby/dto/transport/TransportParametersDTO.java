package by.av.test.testavby.dto.transport;

import by.av.test.testavby.enums.EBodyType;
import by.av.test.testavby.enums.ETransmissionType;
import by.av.test.testavby.enums.ETypeEngine;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TransportParametersDTO {

    @NotEmpty(message = "Body type cannot be empty")
    private EBodyType eBodyType;

    @NotEmpty(message = "Transmission type cannot be empty")
    private ETransmissionType eTransmissionType;

    @NotEmpty(message = "Engine type cannot be empty")
    private ETypeEngine eTypeEngine;

    @NotEmpty(message = "Engine power cannot be empty")
    private Double enginePower;

    private GenerationTransportDTO generationTransport;
}
