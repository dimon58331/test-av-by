package by.av.test.testavby.entity.transport;

import by.av.test.testavby.entity.Post;
import by.av.test.testavby.enums.EBodyType;
import by.av.test.testavby.enums.ETransmissionType;
import by.av.test.testavby.enums.ETypeEngine;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "transport_parameters", uniqueConstraints = @UniqueConstraint(columnNames = {"body_type", "engine_type",
        "transmission_type", "engine_power", "generation_transport_id"
}))
@Data
public class TransportParameters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "body_type")
    private EBodyType eBodyType;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "transmission_type")
    private ETransmissionType eTransmissionType;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "engine_type")
    private ETypeEngine eTypeEngine;

    @Column(name = "engine_power")
    private Double enginePower;

    @ManyToOne
    @JoinColumn(name = "generation_transport_id")
    private GenerationTransport generationTransport;

    @OneToMany(mappedBy = "transportParameters", cascade = CascadeType.ALL)
    private List<Post> post;

    @Column(name = "release_year")
    private Integer releaseYear;
}
