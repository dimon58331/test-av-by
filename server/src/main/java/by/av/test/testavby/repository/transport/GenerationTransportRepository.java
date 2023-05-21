package by.av.test.testavby.repository.transport;

import by.av.test.testavby.entity.transport.GenerationTransport;
import by.av.test.testavby.entity.transport.TransportModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenerationTransportRepository extends JpaRepository<GenerationTransport, Long> {
    List<GenerationTransport> findAllByTransportModelOrderByStartReleaseYear(TransportModel transportModel);

    List<GenerationTransport> findAllByOrderByStartReleaseYear();

    List<GenerationTransport> findAllByOrderByEndReleaseYearDesc();

    List<GenerationTransport> findAllByTransportModelOrderByEndReleaseYearDesc(TransportModel transportModel);

    Page<GenerationTransport> findAllByTransportModelAndEndReleaseYearGreaterThanEqualAndStartReleaseYearLessThanEqual(
            TransportModel transportModel, Integer endReleaseYear, Integer startReleaseYear, Pageable pageable);

    List<GenerationTransport> findAllByEndReleaseYearGreaterThanEqualAndStartReleaseYearLessThanEqual(
            Integer endReleaseYear, Integer startReleaseYear);
}