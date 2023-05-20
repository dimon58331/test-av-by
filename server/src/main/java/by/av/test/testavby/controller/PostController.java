package by.av.test.testavby.controller;

import by.av.test.testavby.dto.PostDTO;
import by.av.test.testavby.entity.Post;
import by.av.test.testavby.mapper.post.PostMapper;
import by.av.test.testavby.payload.response.MessageResponse;
import by.av.test.testavby.service.PostService;
import by.av.test.testavby.validator.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;
    private final PostMapper postMapper;
    private final ResponseErrorValidation responseErrorValidation;
    private final Logger LOG = LoggerFactory.getLogger(PostController.class);

    @Autowired
    public PostController(PostService postService, PostMapper postMapper,
                          ResponseErrorValidation responseErrorValidation) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping(value = "/all", params = {"page", "size", "minPrice", "maxPrice"})
    public Page<PostDTO> getAllPostsByMinAndMaxPrice(@RequestParam("page") int page, @RequestParam("size") int size,
                                                     @RequestParam("minPrice") double minPrice,
                                                     @RequestParam("maxPrice") double maxPrice) {
        return postService.getAllPostsByMinAndMaxPrice(page, size, minPrice, maxPrice)
                .map(postMapper::convertPostToPostDTO);
    }

    @GetMapping(value = "/all", params = {"page", "size"})
    public Page<PostDTO> getAllPosts(@RequestParam("page") int page, @RequestParam("size") int size) {
        LOG.info("page: " + page + ", size: " + size);
        return postService.getAllPosts(page, size).map(postMapper::convertPostToPostDTO);
    }

    @GetMapping(value = "/all", params = {"page", "size", "brandId"})
    public Page<PostDTO> getAllPostsByBrand(@RequestParam("page") int page, @RequestParam("size") int size,
                                            @RequestParam("brandId") int brandId) {
        return postService.getAllPostsByBrand(brandId, page, size).map(postMapper::convertPostToPostDTO);
    }

    @GetMapping(value = "/all", params = {"page", "size", "modelId"})
    public Page<PostDTO> getAllPostsByModel(@RequestParam("page") int page, @RequestParam("size") int size,
                                            @RequestParam("modelId") Long modelId){
        return postService.getAllPostsByModel(modelId, page, size).map(postMapper::convertPostToPostDTO);
    }

    @GetMapping(value = "/all", params = {"page", "size", "generationId"})
    public Page<PostDTO> getAllPostsByGenerationTransport(@RequestParam("page") int page, @RequestParam("size") int size,
                                                          @RequestParam("generationId") Long generationId){
        return postService.getAllPostsByGenerationTransport(generationId, page, size)
                .map(postMapper::convertPostToPostDTO);
    }

    @GetMapping(value = "/all", params = {"page", "size", "transportParametersId"})
    public Page<PostDTO> getAllPostsByTransportParameters(@RequestParam("page") int page, @RequestParam("size") int size,
                                                          @RequestParam("transportParametersId") Long parametersId){
        return postService.getAllPostsByTransportParameters(parametersId, page, size)
                .map(postMapper::convertPostToPostDTO);
    }

    @GetMapping(value = "/user/posts", params = {"page", "size"})
    public Page<PostDTO> getAllPostsByPrincipal(@RequestParam("page") int page, @RequestParam("size") int size,
                                                Principal principal) {
        return postService.getAllPostsByPrincipal(principal, page, size).map(postMapper::convertPostToPostDTO);
    }


    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDTO postDTO, BindingResult bindingResult,
                                             Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (Objects.nonNull(errors)) return errors;

        Post createdPost = postService.createPost(postMapper.convertPostDTOToPost(postDTO), principal);
        return ResponseEntity.ok(postMapper.convertPostToPostDTO(createdPost));
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Object> addFavoritePost(@PathVariable("postId") String postId, Principal principal) {
        return postService.likePost(Long.parseLong(postId), principal)
                ? ResponseEntity.ok(new MessageResponse("Post liked successfully"))
                : ResponseEntity.ok(new MessageResponse("Post unliked successfully"));
    }

    @PostMapping("/{postId}/{transportParametersId}/add")
    public ResponseEntity<Object> addTransportParametersToPost(@PathVariable("transportParametersId") String transportId,
                                                               @PathVariable("postId") String postId,
                                                               Principal principal) {
        postService.addTransportParametersToPost(Long.parseLong(postId), Long.parseLong(transportId), principal);

        return ResponseEntity.ok(new MessageResponse("Transport added to post successfully"));
    }

    @PatchMapping("/{postId}/update")
    public ResponseEntity<Object> updatePost(@PathVariable("postId") String postId, @Valid @RequestBody PostDTO postDTO,
                                             BindingResult result, Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (Objects.nonNull(errors)) return errors;

        postDTO.setId(Long.parseLong(postId));
        Post updatedPost = postService.updateByPostAndPrincipal(postMapper.convertPostDTOToPost(postDTO), principal);

        return ResponseEntity.ok(postMapper.convertPostToPostDTO(updatedPost));
    }

    @DeleteMapping("/{postId}/delete")
    public ResponseEntity<Object> deletePost(@PathVariable("postId") String postId, Principal principal) {
        postService.deletePostByIdAndPrincipal(Long.parseLong(postId), principal);

        return ResponseEntity.ok(new MessageResponse("Post deleted successfully"));
    }
}
