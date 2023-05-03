package by.av.test.testavby.service;

import by.av.test.testavby.entity.Post;
import by.av.test.testavby.entity.User;
import by.av.test.testavby.exception.PostNotFoundException;
import by.av.test.testavby.exception.UserNotFoundException;
import by.av.test.testavby.repository.PostRepository;
import by.av.test.testavby.repository.UserRepository;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final Logger LOG = LoggerFactory.getLogger(PostService.class);

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Post createPost(Post post, Principal principal){
        post.setUser(convertPrincipalToUser(principal));
        return postRepository.save(post);
    }

    public Page<Post> getAllPosts(int page, int size){
        return postRepository.findAllByOrderByCreatedDateDesc(PageRequest.of(page, size));
    }

    public Page<Post> getAllPostsByPrincipal(Principal principal, int page, int size){
        return postRepository.findPostsByUserOrderByCreatedDateDesc(convertPrincipalToUser(principal),
                PageRequest.of(page, size));
    }

    @Transactional
    public void deletePostByIdAndPrincipal(Long postId, Principal principal){
        postRepository.deletePostByIdAndUser(postId, convertPrincipalToUser(principal));
    }

    @Transactional
    public Post updateByPostAndPrincipal(Post post, Principal principal){
        Post updatedPost = postRepository.findPostByIdAndUser(post.getId(), convertPrincipalToUser(principal))
                .orElseThrow(()->new PostNotFoundException("Post cannot be update"));

        updatedPost.setTransport(post.getTransport());
        updatedPost.setPrice(post.getPrice());
        updatedPost.setTitle(post.getTitle());
        updatedPost.setCaption(post.getCaption());

        return postRepository.save(updatedPost);
    }

    @Transactional
    public Post updateByPostAndPostId(Post post, Long postId){
        post.setId(postId);
        return postRepository.save(post);
    }

    @Transactional
    public boolean likePost(Long postId, Principal principal){
        Post post = postRepository.findById(postId).orElseThrow(()->new PostNotFoundException("Post cannot be found"));
        User currentUser = convertPrincipalToUser(principal);
        if (currentUser.getPosts().stream().noneMatch(post1 -> Objects.equals(post1.getId(), post.getId()))){
            throw new PostNotFoundException("Post doesn't belong to the user");
        }
        if (post.getLikedUsers().contains(currentUser.getEmail())){
            post.getLikedUsers().remove(currentUser.getEmail());
            return false;
        } else {
            post.getLikedUsers().add(currentUser.getEmail());
            return true;
        }
    }

    private User convertPrincipalToUser(Principal principal) {
        return userRepository.findUserByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User with email " + principal.getName() + " not found"));
    }
}
