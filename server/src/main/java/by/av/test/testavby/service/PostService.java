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
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        List<Post> allPosts = postRepository.findAllByOrderByCreatedDateDesc();
        return new PageImpl<>(allPosts, PageRequest.of(page, size), allPosts.size());
    }

    public Page<Post> getAllPostsByParameters(Integer page, Integer size, Double minPrice, Double maxPrice, Integer brandId,
                                              Long modelId, Long generationTransportId, List<Long> transportParametersId) {
        List<Post> posts = null;
        if (Objects.nonNull(page) && Objects.nonNull(size)) {
            posts = postRepository.findAllByOrderByCreatedDateDesc().stream().toList();

            if (Objects.nonNull(minPrice) || Objects.nonNull(maxPrice)){
                try {
                    LOG.info("By minPrice and maxPrice: " + minPrice + ", " + maxPrice);

                    if (Objects.isNull(minPrice)) {
                        minPrice = 0.0;
                    } else if (Objects.isNull(maxPrice)) {
                        maxPrice = Double.MAX_VALUE;
                    }

                    List<Post> postList = postRepository.findAllByPriceBetweenOrderByCreatedDateDesc(minPrice, maxPrice)
                            .stream().toList();

                    posts = getPosts(posts, postList);

                } catch (Exception e) {
                    throw new PostNotFoundException("Posts with this max " + maxPrice + " and min " + minPrice
                            + " prices not found");
                }
            }
            if (Objects.nonNull(brandId)) {
                try {
                    LOG.info("By brandId: " + brandId);
                    List<Post> postList = postRepository.findAllByOrderByCreatedDateDesc().stream()
                            .filter(post -> post.getTransportParameters().getGenerationTransport().getTransportModel()
                                    .getTransportBrand().getId().equals(brandId)).toList();
                    posts = getPosts(posts, postList);

                } catch (Exception e) {
                    throw new PostNotFoundException("Posts with this brand id " + brandId + " not found");
                }
            }
            if (Objects.nonNull(modelId)) {
                LOG.info("By modelId");
                try {
                    List<Post> postList = postRepository.findAllByOrderByCreatedDateDesc().stream()
                            .filter(post -> post.getTransportParameters().getGenerationTransport().getTransportModel().getId()
                                    .equals(modelId)).toList();

                    posts = getPosts(posts, postList);

                } catch (Exception e){
                    throw new PostNotFoundException("Posts with this model id " + modelId + " not found");
                }
            }
            if (Objects.nonNull(generationTransportId)) {
                try {
                    LOG.info("By generationTransportId");
                    List<Post> postList = postRepository.findAllByOrderByCreatedDateDesc().stream()
                            .filter(post -> post.getTransportParameters().getGenerationTransport().getId()
                                    .equals(generationTransportId)).toList();

                    posts = getPosts(posts, postList);

                } catch (Exception e){
                    throw new PostNotFoundException("Posts with this generation id " + generationTransportId + " not found");
                }
            }

            if (Objects.nonNull(transportParametersId)) {
                try {
                    LOG.info("By transportParametersId");
                    List<Post> postList = new ArrayList<>();
                    for (Long transportId : transportParametersId) {
                       postList.addAll(posts.stream()
                               .filter(post -> post.getTransportParameters().getId().equals(transportId)).toList());
                    }

                    sortPostsByCreatedDateDesc(postList);

                    posts = getPosts(posts, postList);

                } catch (Exception e) {
                    throw new PostNotFoundException("Posts with this transport parameters id " + transportParametersId
                            + " not found");
                }
            }

        }

        LOG.info(page + " - page, " + size + " - size");
        LOG.info((new PageImpl<>(posts, PageRequest.of(page, size), posts.size())).toString());

        PagedListHolder<Post> pagedListHolder = new PagedListHolder<>(posts);


        pagedListHolder.setPage(page);
        pagedListHolder.setPageSize(size);

        return new PageImpl<>(pagedListHolder.getPageList(), PageRequest.of(page, size), posts.size());
    }


    public Page<Post> getAllPostsByPrincipal(Principal principal, int page, int size) {
        return postRepository.findPostsByUserOrderByCreatedDateDesc(convertPrincipalToUser(principal),
                PageRequest.of(page, size));
    }

    private User convertPrincipalToUser(Principal principal) {
        return customUserRepository.findUserByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User with email " + principal.getName() + " not found"));
    }

    private List<Post> getPosts(List<Post> posts, List<Post> postList) throws Exception {
        if (postList.isEmpty()) {
            throw new Exception();
        }

        List<Post> equalsPosts = new ArrayList<>();
        for (Post post : postList) {
            equalsPosts.addAll(posts.stream().filter(post1 -> post1.getId().equals(post.getId())).toList());
        }
        if (!equalsPosts.isEmpty()) {
            posts = equalsPosts;
        }
        return posts;
    }

    private void sortPostsByCreatedDateDesc(List<Post> unsortedPosts) {
        Comparator<Post> byCreatedDate = Comparator.comparing(Post::getCreatedDate);
        unsortedPosts.sort(byCreatedDate);
        Collections.reverse(unsortedPosts);
    }
}
