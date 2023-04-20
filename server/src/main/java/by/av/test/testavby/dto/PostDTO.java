package by.av.test.testavby.dto;

import by.av.test.testavby.entity.Transport;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostDTO {

    private Long id;
    @NotNull(message = "Transport must be filled in")
    private Transport transport;
    @NotNull(message = "Price must be filled int")
    @Min(value = 0, message = "Price cannot be less than 0")
    private Double price;
    private String title;
    private String caption;
}
