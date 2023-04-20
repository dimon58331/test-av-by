package by.av.test.testavby.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Transport")
@Data
public class Transport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "model", nullable = false)
    @NotEmpty(message = "Model cannot be empty")
    /*TODO
    *  Create model table and fill it. Create model entity (or ENUM)
    * */
    private String model;
    @Column(name = "brand", nullable = false)
    @NotEmpty(message = "Brand cannot be empty")
    /*TODO
     *  Create brand table and fill it. Create brand entity (or ENUM)
     * */
    private String brand;
    @Column(name = "release_date", nullable = false)
    @NotNull(message = "Release date cannot be null")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date releaseDate;
    @Column(name = "engine_type", nullable = false)
    @NotEmpty(message = "Engine type cannot be empty")
    /*TODO
     *  Create engine type table and fill it. Create engine type entity (or ENUM)
     * */
    private String engineType;
    @Column(name = "engine_capacity", nullable = false)
    @NotNull(message = "Engine capacity cannot be null")
    @Min(value = 0, message = "Engine capacity cannot be less than 0")
    private Double engineCapacity;
    @Column(name = "engine_power", nullable = false)
    @NotNull(message = "Engine power cannot be null")
    @Min(value = 0, message = "Engine power cannot be less than 0")
    private Double enginePower;
    @Column(name = "color", nullable = false)
    @NotEmpty(message = "Color cannot be empty")
    private String color;
    /*
     * TODO
     *  Check Cascade Type
     * */
    @ManyToMany(mappedBy = "favoriteTransport")
    private Set<User> favoriteUsers = new HashSet<>();
    /*
     * TODO
     *  Check Cascade Type
     * */
    @OneToOne(mappedBy = "transport")
    private Post post;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(updatable = false, name = "created_date")
    private LocalDateTime createdDate;
    @PrePersist
    protected void onCreate(){
        this.createdDate = LocalDateTime.now();
    }
}
