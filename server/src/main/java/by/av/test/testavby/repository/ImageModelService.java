package by.av.test.testavby.repository;

import by.av.test.testavby.entity.ImageModel;
import by.av.test.testavby.entity.Post;
import by.av.test.testavby.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ImageModelService extends JpaRepository<ImageModel, Long> {
    public Optional<ImageModel> findImageModelByUser(User user);
    public Optional<ImageModel> findImageModelByPost(Post post);
}
