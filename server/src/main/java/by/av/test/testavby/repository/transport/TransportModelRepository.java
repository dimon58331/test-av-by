package by.av.test.testavby.repository.transport;

import by.av.test.testavby.entity.transport.TransportBrand;
import by.av.test.testavby.entity.transport.TransportModel;
import by.av.test.testavby.entity.transport.TransportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransportModelRepository extends JpaRepository<TransportModel, Long> {
    Optional<TransportModel> findByTransportBrandAndTransportType(TransportBrand brand, TransportType type);
}
