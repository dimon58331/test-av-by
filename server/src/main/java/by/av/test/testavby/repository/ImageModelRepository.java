package by.av.test.testavby.repository;

import by.av.test.testavby.entity.ImageModel;
import by.av.test.testavby.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageModelRepository extends JpaRepository<ImageModel, Long> {
    //public Optional<ImageModel> findImageModelByUser(User user);
   Optional<ImageModel> findImageModelByPost(Post post);
}
