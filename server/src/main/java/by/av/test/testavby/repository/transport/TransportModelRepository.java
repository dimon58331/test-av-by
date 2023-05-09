package by.av.test.testavby.repository.transport;

import by.av.test.testavby.entity.transport.TransportBrand;
import by.av.test.testavby.entity.transport.TransportModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportModelRepository extends JpaRepository<TransportModel, Long> {
    List<TransportModel> findAllByTransportBrand(TransportBrand brand);
    Page<TransportModel> findAllByTransportBrand(TransportBrand transportBrand, Pageable pageable);
}
