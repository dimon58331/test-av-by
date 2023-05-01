package by.av.test.testavby.repository.transport;

import by.av.test.testavby.entity.transport.TransportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransportTypeRepository extends JpaRepository<TransportType, Integer> {
    Optional<TransportType> findByTypeName(String typeName);
}
