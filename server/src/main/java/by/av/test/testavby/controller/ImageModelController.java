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
    public ResponseEntity<Object> uploadImageToPost(@PathVariable("postId") String postId,
                                                    @RequestParam("file") MultipartFile file) throws IOException {
        imageModelService.uploadImageToPost(Long.parseLong(postId), file);

        return ResponseEntity.ok(new MessageResponse("Image uploaded successfully"));
    }
}
