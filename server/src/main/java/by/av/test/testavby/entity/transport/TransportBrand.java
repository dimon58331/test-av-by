package by.av.test.testavby.entity.transport;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "transport_brand")
@Data
public class TransportBrand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "brand_name", unique = true)
    private String brandName;

    @OneToMany(mappedBy = "transportBrand", cascade = CascadeType.ALL)
    private List<TransportModel> transportModels;
}
