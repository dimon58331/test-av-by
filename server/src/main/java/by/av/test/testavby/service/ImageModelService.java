package by.av.test.testavby.service;

import by.av.test.testavby.entity.ImageModel;
import by.av.test.testavby.entity.Post;
import by.av.test.testavby.entity.User;
import by.av.test.testavby.exception.ImageModelNotFoundException;
import by.av.test.testavby.exception.PostNotFoundException;
import by.av.test.testavby.exception.UserNotFoundException;
import by.av.test.testavby.repository.ImageModelRepository;
import by.av.test.testavby.repository.PostRepository;
import by.av.test.testavby.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@Transactional(readOnly = true)
public class ImageModelService {
    private final ImageModelRepository imageModelRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public ImageModelService(ImageModelRepository imageModelRepository, PostRepository postRepository, UserRepository userRepository) {
        this.imageModelRepository = imageModelRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void uploadImageToPost(Long postId, MultipartFile file) throws IOException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post cannot be found"));

        Optional<ImageModel> imageModelOptional = imageModelRepository.findImageModelByPost(post);
        imageModelOptional.ifPresent(imageModelRepository::delete);

        ImageModel imageModel = new ImageModel();
        imageModel.setPost(post);
        imageModel.setName(file.getOriginalFilename());
        imageModel.setImageBytes(compressBytes(file.getBytes()));

        imageModelRepository.save(imageModel);
    }

    public ImageModel getPostImage(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        ImageModel imageModel = imageModelRepository.findImageModelByPost(post).orElseThrow(
                () -> new ImageModelNotFoundException("Image cannot be found")
        );
        imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        return imageModel;
    }

    private User convertPrincipalToUser(Principal principal) {
        return userRepository.findUserByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User with email " + principal.getName() + " not found"));
    }

    private byte[] compressBytes(byte[] data){
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()){
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        }catch (IOException ignored){
        }
        return outputStream.toByteArray();
    }

    private static byte[] decompressBytes(byte[] data){
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while(!inflater.finished()){
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        }catch (IOException | DataFormatException ignored){
        }
        return outputStream.toByteArray();
    }
}
