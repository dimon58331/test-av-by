package by.av.test.testavby.repository;

import by.av.test.testavby.entity.Post;
import by.av.test.testavby.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PostService extends JpaRepository<Post, Long> {
    public List<Post> findPostsByUserOrderByCreatedDateDesc(User user);
    public List<Post> findAllByOrderByCreatedDateDesc();
    public Optional<Post> findPostByIdAndUser(Long postId, User user);
}
