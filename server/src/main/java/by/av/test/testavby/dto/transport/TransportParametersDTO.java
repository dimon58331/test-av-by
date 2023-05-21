package by.av.test.testavby.dto.transport;

import by.av.test.testavby.enums.EBodyType;
import by.av.test.testavby.enums.ETransmissionType;
import by.av.test.testavby.enums.ETypeEngine;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransportParametersDTO {
    private Long id;

    private EBodyType bodyType;

    private ETransmissionType transmissionType;

    private ETypeEngine typeEngine;

    @NotNull(message = "Engine power cannot be empty")
    private Double enginePower;

    @NotNull(message = "Release year cannot be empty")
    private Integer releaseYear;
}
