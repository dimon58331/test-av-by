package by.av.test.testavby.repository;

import by.av.test.testavby.entity.Post;
import by.av.test.testavby.entity.Transport;
import by.av.test.testavby.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TransportRepository extends JpaRepository<Transport, Long> {
    public Optional<Transport> findTransportByPost(Post post);
}
