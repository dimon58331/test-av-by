package by.av.test.testavby.repository;

import by.av.test.testavby.entity.Post;
import by.av.test.testavby.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PostRepository extends JpaRepository<Post, Long> {
    public Page<Post> findPostsByUserOrderByCreatedDateDesc(User user, Pageable pageable);
    public Page<Post> findAllByOrderByCreatedDateDesc(Pageable pageable);
    public Optional<Post> findPostByIdAndUser(Long postId, User user);
    public void deletePostByIdAndUser(Long postId, User user);
}
