package by.av.test.testavby.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
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
    /*TODO
    *  Create model table and fill it. Create model entity (or ENUM)
    * */
    private String model;
    @Column(name = "brand", nullable = false)
    /*TODO
     *  Create brand table and fill it. Create brand entity (or ENUM)
     * */
    private String brand;
    @Column(name = "release_year", nullable = false)
    private Integer releaseYear;
    @Column(name = "engine_type", nullable = false)
    /*TODO
     *  Create engine type table and fill it. Create engine type entity (or ENUM)
     * */
    private String engineType;
    @Column(name = "engine_capacity", nullable = false)
    private Double engineCapacity;
    @Column(name = "engine_power", nullable = false)
    private Double enginePower;
    @Column(name = "color", nullable = false)
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
    @Column(updatable = false, name = "created_date")
    private LocalDateTime createdDate;
    @PrePersist
    protected void onCreate(){
        this.createdDate = LocalDateTime.now();
    }
}
