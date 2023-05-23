package by.av.test.testavby.repository;

import by.av.test.testavby.entity.Post;
import by.av.test.testavby.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findPostsByUserOrderByCreatedDateDesc(User user, Pageable pageable);

    List<Post> findAllByOrderByCreatedDateDesc();

    List<Post> findAllByPriceBetweenOrderByCreatedDateDesc(Double minPrice, Double maxPrice);

    Optional<Post> findPostByIdAndUser(Long postId, User user);
}
