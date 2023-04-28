package by.av.test.testavby.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransportDTO {
    private Long id;
    @NotEmpty(message = "Model cannot be empty")
    private String model;
    @NotEmpty(message = "Brand cannot be empty")
    private String brand;
    @NotNull(message = "Release date cannot be null")
    private Integer releaseYear;
    @NotEmpty(message = "Engine type cannot be empty")
    private String engineType;
    @NotNull(message = "Engine capacity cannot be null")
    @Min(value = 0, message = "Engine capacity cannot be less than 0")
    private Double engineCapacity;
    @NotNull(message = "Engine power cannot be null")
    @Min(value = 0, message = "Engine power cannot be less than 0")
    private Double enginePower;
    @NotEmpty(message = "Color cannot be empty")
    private String color;
}
