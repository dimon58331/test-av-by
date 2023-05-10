package by.av.test.testavby.repository.transport;

import by.av.test.testavby.entity.transport.TransportBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransportBrandRepository extends JpaRepository<TransportBrand, Integer> {
}
