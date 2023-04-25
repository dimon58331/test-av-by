package by.av.test.testavby.service;

import by.av.test.testavby.entity.Post;
import by.av.test.testavby.entity.Transport;
import by.av.test.testavby.exception.PostNotFoundException;
import by.av.test.testavby.exception.TransportExistsException;
import by.av.test.testavby.exception.TransportNotFoundException;
import by.av.test.testavby.repository.PostRepository;
import by.av.test.testavby.repository.TransportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class TransportService {
    private final TransportRepository transportRepository;
    private final PostRepository postRepository;

    @Autowired
    public TransportService(TransportRepository transportRepository, PostRepository postRepository) {
        this.transportRepository = transportRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public Transport createTransportForPost(Long postId, Transport transport){
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        if (Objects.nonNull(post.getTransport())){
            throw new TransportExistsException("Transport for this post already exists");
        }
        post.setTransport(transport);
        return transportRepository.save(transport);
    }

    public List<Transport> getAllTransport(){
        return transportRepository.findAll();
    }

    public Transport getTransportByPostId(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        return transportRepository.findTransportByPost(post).orElseThrow(
                () -> new TransportNotFoundException("Transport cannot be found")
        );
    }
}
