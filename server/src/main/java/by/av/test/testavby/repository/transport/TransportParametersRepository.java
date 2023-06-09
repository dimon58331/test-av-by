package by.av.test.testavby.repository.transport;

import by.av.test.testavby.entity.Post;
import by.av.test.testavby.entity.transport.GenerationTransport;
import by.av.test.testavby.entity.transport.TransportParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransportParametersRepository extends JpaRepository<TransportParameters, Long> {
    Optional<TransportParameters> findTransportParametersByPost(Post post);

    List<TransportParameters> findAllByGenerationTransportOrderByEnginePower(GenerationTransport generationTransport);

    List<TransportParameters> findAllByReleaseYearBetween(Integer minReleaseYear, Integer maxReleaseYear);

    Optional<TransportParameters> findFirstByOrderByReleaseYear();

    Optional<TransportParameters> findFirstByOrderByReleaseYearDesc();
}
