package by.av.test.testavby.entity.transport;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(
    name = "transport_model",
    uniqueConstraints = @UniqueConstraint(columnNames = {"transport_type_id", "transport_brand_id"})
)
@Data
public class TransportModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transport_type_id", referencedColumnName = "id")
    private TransportType transportType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transport_brand_id", referencedColumnName = "id")
    private TransportBrand transportBrand;

    @OneToMany(mappedBy = "transportModel")
    private List<Transport> transport;
}