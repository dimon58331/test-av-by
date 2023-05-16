package by.av.test.testavby.controller.admin;

import by.av.test.testavby.dto.PostDTO;
import by.av.test.testavby.entity.Post;
import by.av.test.testavby.mapper.post.PostMapper;
import by.av.test.testavby.payload.response.MessageResponse;
import by.av.test.testavby.service.PostService;
import by.av.test.testavby.validator.ResponseErrorValidation;
import jakarta.validation.Valid;
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
    private final PostMapper postMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public AdminPostController(PostService postService, PostMapper postMapper,
                               ResponseErrorValidation responseErrorValidation) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PatchMapping("/{postId}/update")
    public ResponseEntity<Object> updatePost(@PathVariable("postId") String postId, @Valid @RequestBody PostDTO postDTO,
                                             BindingResult result) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (Objects.nonNull(errors)) return errors;

        postDTO.setId(Long.parseLong(postId));
        Post updatedPost = postService.updateByPostAndPostId(postMapper.convertPostDTOToPost(postDTO), Long.parseLong(postId));

        return ResponseEntity.ok(postMapper.convertPostToPostDTO(updatedPost));
    }

    @DeleteMapping("/{postId}/delete")
    public ResponseEntity<Object> deletePost(@PathVariable("postId") String postId) {
        postService.deletePostById(Long.parseLong(postId));

        return ResponseEntity.ok(new MessageResponse("Post deleted successfully"));
    }
}
