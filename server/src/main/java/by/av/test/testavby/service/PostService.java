package by.av.test.testavby.service;

import by.av.test.testavby.entity.Post;
import by.av.test.testavby.entity.User;
import by.av.test.testavby.entity.transport.TransportParameters;
import by.av.test.testavby.exception.PostNotFoundException;
import by.av.test.testavby.exception.TransportExistsException;
import by.av.test.testavby.exception.TransportNotFoundException;
import by.av.test.testavby.exception.UserNotFoundException;
import by.av.test.testavby.repository.PostRepository;
import by.av.test.testavby.repository.CustomUserRepository;
import by.av.test.testavby.repository.transport.TransportParametersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final TransportParametersRepository transportParametersRepository;
    private final CustomUserRepository customUserRepository;
    private final Logger LOG = LoggerFactory.getLogger(PostService.class);

    @Autowired
    public PostService(PostRepository postRepository, TransportParametersRepository transportParametersRepository,
                       CustomUserRepository customUserRepository) {
        this.postRepository = postRepository;
        this.transportParametersRepository = transportParametersRepository;
        this.customUserRepository = customUserRepository;
    }

    @Transactional
    public Post createPost(Post post, Principal principal) {
        post.setUser(convertPrincipalToUser(principal));
        return postRepository.save(post);
    }

    @Transactional
    public void addTransportParametersToPost(Long postId, Long transportParametersId, Principal principal) {
        Post post = postRepository.findPostByIdAndUser(postId, convertPrincipalToUser(principal))
                .orElseThrow(() -> new PostNotFoundException("Post for current user cannot be found"));
        if (Objects.nonNull(post.getTransportParameters())) {
            throw new TransportExistsException("Transport for this post already added");
        }
        TransportParameters transportParameters = transportParametersRepository.findById(transportParametersId)
                .orElseThrow(() -> new TransportNotFoundException("Transport cannot be found"));
        post.setTransportParameters(transportParameters);
    }

    @Transactional
    public void deletePostByIdAndPrincipal(Long postId, Principal principal) {
        postRepository.delete(postRepository.findPostByIdAndUser(postId, convertPrincipalToUser(principal))
                .orElseThrow(() -> new PostNotFoundException("Post for current user not found")));
    }

    @Transactional
    public Post updateByPostAndPrincipal(Post post, Principal principal) {
        Post updatedPost = postRepository.findPostByIdAndUser(post.getId(), convertPrincipalToUser(principal))
                .orElseThrow(() -> new PostNotFoundException("Post cannot be update"));

        updatedPost.setTransportParameters(post.getTransportParameters());
        updatedPost.setPrice(post.getPrice());
        updatedPost.setTitle(post.getTitle());
        updatedPost.setCaption(post.getCaption());

        return postRepository.save(updatedPost);
    }

    @Transactional
    public Post updateByPostAndPostId(Post post, Long postId) {
        Post oldPost = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found"));
        oldPost.setCaption(post.getCaption());
        oldPost.setPrice(post.getPrice());
        oldPost.setTitle(post.getTitle());
        return postRepository.save(oldPost);
    }

    @Transactional
    public boolean likePost(Long postId, Principal principal) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        User currentUser = convertPrincipalToUser(principal);
        if (currentUser.getPosts().stream().noneMatch(post1 -> Objects.equals(post1.getId(), post.getId()))) {
            throw new PostNotFoundException("Post doesn't belong to the user");
        }
        if (post.getLikedUsers().contains(currentUser.getEmail())) {
            post.getLikedUsers().remove(currentUser.getEmail());
            return false;
        } else {
            post.getLikedUsers().add(currentUser.getEmail());
            return true;
        }
    }

    @Transactional
    public void deletePostById(Long postId) {
        postRepository.delete(postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found")));
    }

    public Page<Post> getAllPosts(int page, int size) {
        return postRepository.findAllByOrderByCreatedDateDesc(PageRequest.of(page, size));
    }

    public Page<Post> getAllPostsByBrand(Integer brandId, int page, int size) {
        try {
            List<Post> posts = postRepository.findAllByOrderByCreatedDateDesc(PageRequest.of(page, size)).stream()
                    .filter(post -> post.getTransportParameters().getGenerationTransport().getTransportModel()
                            .getTransportBrand().getId().equals(brandId)).toList();
            return new PageImpl<>(posts);
        } catch (Exception e) {
            throw new PostNotFoundException("Posts with this brand id " + brandId + " not found");
        }
    }

    public Page<Post> getAllPostsByModel(Long modelId, int page, int size) {
        try {
            List<Post> posts = postRepository.findAllByOrderByCreatedDateDesc(PageRequest.of(page, size)).stream()
                    .filter(post -> post.getTransportParameters().getGenerationTransport().getTransportModel().getId()
                            .equals(modelId)).toList();
            return new PageImpl<>(posts);
        } catch (Exception e){
            throw new PostNotFoundException("Posts with this model id " + modelId + " not found");
        }
    }

    public Page<Post> getAllPostsByGenerationTransport(Long generationTransportId, int page, int size) {
        try {
            List<Post> posts = postRepository.findAllByOrderByCreatedDateDesc(PageRequest.of(page, size)).stream()
                    .filter(post -> post.getTransportParameters().getGenerationTransport().getId()
                            .equals(generationTransportId)).toList();
            return new PageImpl<>(posts);
        } catch (Exception e){
            throw new PostNotFoundException("Posts with this generation id " + generationTransportId + " not found");
        }
    }

    public Page<Post> getAllPostsByTransportParameters(Long transportParametersId, int page, int size) {
        try {
            List<Post> posts = postRepository.findAllByOrderByCreatedDateDesc(PageRequest.of(page, size)).stream()
                    .filter(post -> post.getTransportParameters().getId().equals(transportParametersId)).toList();
            return new PageImpl<>(posts);
        } catch (Exception e) {
            throw new PostNotFoundException("Posts with this parameters id " + transportParametersId + " not found");
        }
    }

    public Page<Post> getAllPostsByPrincipal(Principal principal, int page, int size) {
        return postRepository.findPostsByUserOrderByCreatedDateDesc(convertPrincipalToUser(principal),
                PageRequest.of(page, size));
    }

    private User convertPrincipalToUser(Principal principal) {
        return customUserRepository.findUserByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User with email " + principal.getName() + " not found"));
    }
}
