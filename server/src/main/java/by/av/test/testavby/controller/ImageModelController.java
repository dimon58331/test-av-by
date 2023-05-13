package by.av.test.testavby.controller;

import by.av.test.testavby.entity.ImageModel;
import by.av.test.testavby.payload.response.MessageResponse;
import by.av.test.testavby.service.ImageModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("/api/image")
public class ImageModelController {
    private final ImageModelService imageModelService;

    @Autowired
    public ImageModelController(ImageModelService imageModelService) {
        this.imageModelService = imageModelService;
    }

    @PostMapping("/{postId}/upload")
    public ResponseEntity<MessageResponse> uploadImageToPost(@PathVariable("postId") String postId,
                                                             @RequestParam("file") MultipartFile file,
                                                             Principal principal) throws IOException {
        imageModelService.uploadImageToPost(Long.parseLong(postId), file, principal);

        return ResponseEntity.ok(new MessageResponse("Image uploaded successfully"));
    }

    @DeleteMapping("/{postId}/delete")
    public ResponseEntity<MessageResponse> deletePostImage(@PathVariable("postId") String postId, Principal principal) {
        imageModelService.deletePostImage(Long.parseLong(postId), principal);

        return ResponseEntity.ok(new MessageResponse("Post image deleted successfully"));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ImageModel> getPostImage(@PathVariable("postId") String postId) {
        ImageModel imageModel = imageModelService.getPostImage(Long.parseLong(postId));

        return ResponseEntity.ok(imageModel);
    }
}
