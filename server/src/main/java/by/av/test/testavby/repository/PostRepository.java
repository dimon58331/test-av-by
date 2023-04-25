package by.av.test.testavby.repository;

import by.av.test.testavby.entity.Post;
import by.av.test.testavby.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Service
public interface PostRepository extends JpaRepository<Post, Long> {
    public List<Post> findPostsByUserOrderByCreatedDateDesc(User user, Pageable pageable);
    public List<Post> findAllByOrderByCreatedDateDesc(Pageable pageable);
    public Optional<Post> findPostByIdAndUser(Long postId, User user);
    public void deletePostByIdAndUser(Long postId, User user);
}
