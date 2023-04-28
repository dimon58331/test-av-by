package by.av.test.testavby.entity.transport;

import by.av.test.testavby.entity.Post;
import by.av.test.testavby.entity.User;
import by.av.test.testavby.enums.ETypeEngine;
import jakarta.persistence.*;
import lombok.Data;

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

    @Column(name = "engine_capacity", nullable = false)
    private Double engineCapacity;

    @Column(name = "engine_power", nullable = false)
    private Double enginePower;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "release_year", nullable = false)
    private Integer releaseYear;

    @Enumerated(EnumType.ORDINAL)
    private ETypeEngine eTypeEngine;

    @OneToMany(mappedBy = "transport")
    private List<Post> post;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transport_model_id", referencedColumnName = "id")
    private TransportModel transportModel;

    @Column(updatable = false, name = "created_date")
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate(){
        this.createdDate = LocalDateTime.now();
    }
}
