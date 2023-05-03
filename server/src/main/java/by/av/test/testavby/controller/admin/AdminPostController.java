package by.av.test.testavby.controller.admin;

import by.av.test.testavby.dto.PostDTO;
import by.av.test.testavby.entity.Post;
import by.av.test.testavby.payload.response.MessageResponse;
import by.av.test.testavby.service.PostService;
import by.av.test.testavby.validator.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/api/admin/post")
public class AdminPostController {
    private final PostService postService;
    private final ModelMapper modelMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public AdminPostController(PostService postService, ModelMapper modelMapper,
                               ResponseErrorValidation responseErrorValidation) {
        this.postService = postService;
        this.modelMapper = modelMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/{postId}/update")
    public ResponseEntity<Object> updatePost(@PathVariable("postId") String postId, @Valid @RequestBody PostDTO postDTO,
                                             BindingResult result){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (Objects.nonNull(errors)) return errors;

        postDTO.setId(Long.parseLong(postId));
        Post updatedPost = postService.updateByPostAndPostId(convertPostDTOToPost(postDTO), Long.parseLong(postId));

        return ResponseEntity.ok(convertPostToPostDTO(updatedPost));
    }

    @PostMapping("/{postId}/delete")
    public ResponseEntity<Object> deletePost(@PathVariable("postId") String postId, Principal principal){
        postService.deletePostByIdAndPrincipal(Long.parseLong(postId), principal);

        return ResponseEntity.ok(new MessageResponse("Post deleted successfully"));
    }

    private Post convertPostDTOToPost(PostDTO postDTO){
        return modelMapper.map(postDTO, Post.class);
    }

    private PostDTO convertPostToPostDTO(Post post){
        return modelMapper.map(post, PostDTO.class);
    }
}
