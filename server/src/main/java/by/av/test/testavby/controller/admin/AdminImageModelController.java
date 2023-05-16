package by.av.test.testavby.controller.admin;

import by.av.test.testavby.payload.response.MessageResponse;
import by.av.test.testavby.service.ImageModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/api/admin/image")
public class AdminImageModelController {
    private final ImageModelService imageModelService;

    @Autowired
    public AdminImageModelController(ImageModelService imageModelService) {
        this.imageModelService = imageModelService;
    }

    @PostMapping("/post/{postId}/upload")
    public ResponseEntity<MessageResponse> uploadImageToPost(@PathVariable("postId") String postId,
                                                             @RequestParam("file") MultipartFile file) throws IOException {
        imageModelService.uploadImageToAnyPost(Long.parseLong(postId), file);

        return ResponseEntity.ok(new MessageResponse("Image uploaded successfully"));
    }

    @PostMapping("/generation/{generationTransportId}/upload")
    public ResponseEntity<MessageResponse> uploadImageToGenerationTransport(@PathVariable("generationTransportId") String generationTransportId,
                                                                            @RequestParam("file") MultipartFile file) throws IOException{
        imageModelService.uploadImageToGenerationTransport(Long.parseLong(generationTransportId), file);
        return ResponseEntity.ok(new MessageResponse("Image uploaded successfully"));
    }


    @DeleteMapping("/post/{postId}/delete")
    public ResponseEntity<MessageResponse> deleteAnyPostImage(@PathVariable("postId") String postId) {
        imageModelService.deleteAnyPostImage(Long.parseLong(postId));

        return ResponseEntity.ok(new MessageResponse("Post image deleted successfully"));
    }

    @DeleteMapping("/generation/{generationTransportId}/delete")
    public ResponseEntity<MessageResponse> deleteGenerationTransportImage(@PathVariable("generationTransportId")
                                                                              String generationTransportId) {
        imageModelService.deleteGenerationTransportImage(Long.parseLong(generationTransportId));

        return ResponseEntity.ok(new MessageResponse("Post image deleted successfully"));
    }
}
