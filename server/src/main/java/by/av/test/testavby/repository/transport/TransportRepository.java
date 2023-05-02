package by.av.test.testavby.repository.transport;

import by.av.test.testavby.entity.Post;
import by.av.test.testavby.entity.transport.Transport;
import by.av.test.testavby.entity.transport.TransportModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TransportRepository extends JpaRepository<Transport, Long> {
    public Optional<Transport> findTransportByPost(Post post);

    @Query("Select t from Transport t join TransportModel tm on t.transportModel.id = tm.id\n" +
            "join TransportBrand tb on tm.transportBrand.id = tb.id order by tb.brandName")
    public Page<Transport> findAllTransportSortedByAsc(Pageable pageable);
}
