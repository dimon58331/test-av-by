package by.av.test.testavby.entity.transport;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "transport_model")
@Data
public class TransportModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "model_name")
    private String modelName;

    @ManyToOne
    @JoinColumn(name = "transport_brand_id")
    private TransportBrand transportBrand;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transportModel")
    private List<GenerationTransport> generationTransports;

}
