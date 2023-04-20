package by.av.test.testavby.controller;

import by.av.test.testavby.dto.PostDTO;
import by.av.test.testavby.entity.Post;
import by.av.test.testavby.service.PostService;
import by.av.test.testavby.validator.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private final ModelMapper modelMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public PostController(PostService postService, ModelMapper modelMapper, ResponseErrorValidation responseErrorValidation) {
        this.postService = postService;
        this.modelMapper = modelMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDTO postDTO, BindingResult bindingResult,
                                             Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (Objects.nonNull(errors)) return errors;

        Post createdPost = postService.createPost(convertPostDTOToPost(postDTO), principal);
        return new ResponseEntity<>(convertPostToPostDTO(createdPost), HttpStatus.OK);
    }

    private Post convertPostDTOToPost(PostDTO postDTO){
        return modelMapper.map(postDTO, Post.class);
    }

    private PostDTO convertPostToPostDTO(Post post){
        return modelMapper.map(post, PostDTO.class);
    }
}
