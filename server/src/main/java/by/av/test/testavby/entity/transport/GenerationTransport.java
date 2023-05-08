package by.av.test.testavby.entity.transport;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "generation_transport")
@Data
public class GenerationTransport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "generation_name")
    private String generationName;

    @Column(name = "start_release_year")
    private Integer startReleaseYear;

    @Column(name = "end_release_year")
    private Integer endReleaseYear;

    @ManyToOne
    @JoinColumn(name = "transport_model_id")
    private TransportModel transportModel;

    @OneToMany(mappedBy = "generationTransport", cascade = CascadeType.ALL)
    private List<TransportParameters> transportParameters;
}
