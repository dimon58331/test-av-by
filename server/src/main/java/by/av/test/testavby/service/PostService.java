package by.av.test.testavby.service;

import by.av.test.testavby.entity.Post;
import by.av.test.testavby.entity.User;
import by.av.test.testavby.exception.PostNotFoundException;
import by.av.test.testavby.exception.UserNotFoundException;
import by.av.test.testavby.repository.PostRepository;
import by.av.test.testavby.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    /*
    * TODO
    *  Check transport
    * */

    @Transactional
    public Post createPost(Post post, Principal principal){
        post.setUser(convertPrincipalToUser(principal));
        return postRepository.save(post);
    }

    public List<Post> getAllPosts(){
        return postRepository.findAllByOrderByCreatedDateDesc();
    }

    public List<Post> getAllPostsByPrincipal(Principal principal){
        return postRepository.findPostsByUserOrderByCreatedDateDesc(convertPrincipalToUser(principal));
    }

    @Transactional
    public void deletePostByIdAndPrincipal(Long postId, Principal principal){
        postRepository.deletePostByIdAndUser(postId, convertPrincipalToUser(principal));
    }

    @Transactional
    public Post updateByPostAndPrincipal(Post post, Principal principal){
        Optional<Post> postOptional = postRepository.findPostByIdAndUser(post.getId(), convertPrincipalToUser(principal));
        if (postOptional.isEmpty()){
            throw new PostNotFoundException("Post cannot be update");
        }
        Post updatedPost = postOptional.get();
        updatedPost.setTransport(post.getTransport());
        updatedPost.setPrice(post.getPrice());
        updatedPost.setTitle(post.getTitle());
        updatedPost.setCaption(post.getCaption());

        return postRepository.save(updatedPost);
    }

    private User convertPrincipalToUser(Principal principal) {
        return userRepository.findUserByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User with email " + principal.getName() + " not found"));
    }
}
