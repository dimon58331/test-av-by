package by.av.test.testavby.repository;

import by.av.test.testavby.entity.ImageModel;
import by.av.test.testavby.entity.transport.GenerationTransport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageModelRepository extends JpaRepository<ImageModel, Long> {
    Optional<ImageModel> findImageModelByPostId(Long postId);
    Optional<ImageModel> findImageModelByGenerationTransportId(Long generationTransportId);
}
