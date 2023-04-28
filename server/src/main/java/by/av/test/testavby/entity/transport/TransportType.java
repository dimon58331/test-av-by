package by.av.test.testavby.entity.transport;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "transport_type")
@Data
public class TransportType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "type_name", unique = true)
    private String typeName;

    @OneToMany(mappedBy = "transportType")
    private Set<TransportModel> transportModels = new HashSet<>();
}
