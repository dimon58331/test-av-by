package by.av.test.testavby.service;

import by.av.test.testavby.entity.ImageModel;
import by.av.test.testavby.entity.Post;
import by.av.test.testavby.entity.User;
import by.av.test.testavby.entity.transport.GenerationTransport;
import by.av.test.testavby.exception.*;
import by.av.test.testavby.repository.ImageModelRepository;
import by.av.test.testavby.repository.PostRepository;
import by.av.test.testavby.repository.CustomUserRepository;
import by.av.test.testavby.repository.transport.GenerationTransportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final CustomUserRepository customUserRepository;
    private final GenerationTransportRepository generationTransportRepository;
    private final Logger LOG = LoggerFactory.getLogger(ImageModelService.class);

    @Autowired
    public ImageModelService(ImageModelRepository imageModelRepository, PostRepository postRepository,
                             CustomUserRepository customUserRepository, GenerationTransportRepository generationTransportRepository) {
        this.imageModelRepository = imageModelRepository;
        this.postRepository = postRepository;
        this.customUserRepository = customUserRepository;
        this.generationTransportRepository = generationTransportRepository;
    }

    @Transactional
    public void uploadImageToPost(Long postId, MultipartFile file, Principal principal) throws IOException {
        Post post = postRepository.findPostByIdAndUser(postId, convertPrincipalToUser(principal))
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));

        Optional<ImageModel> imageModelOptional = imageModelRepository.findImageModelByPostId(post.getId());
        imageModelOptional.ifPresent(imageModelRepository::delete);

        ImageModel imageModel = new ImageModel();
        imageModel.setPostId(post.getId());
        imageModel.setName(file.getOriginalFilename());
        imageModel.setImageBytes(compressBytes(file.getBytes()));

        imageModelRepository.save(imageModel);
    }

    @Transactional
    public void uploadImageToAnyPost(Long postId, MultipartFile file) throws IOException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post cannot be found"));

        Optional<ImageModel> imageModelOptional = imageModelRepository.findImageModelByPostId(post.getId());
        imageModelOptional.ifPresent(imageModelRepository::delete);

        ImageModel imageModel = new ImageModel();
        imageModel.setPostId(postId);
        imageModel.setName(file.getOriginalFilename());
        imageModel.setImageBytes(compressBytes(file.getBytes()));

        imageModelRepository.save(imageModel);
    }

    @Transactional
    public void uploadImageToGenerationTransport(Long generationTransportId, MultipartFile file) throws IOException {
        GenerationTransport generationTransport = generationTransportRepository.findById(generationTransportId)
                .orElseThrow(() -> new TransportNotFoundException("Generation transport not found"));

        Optional<ImageModel> imageModelOptional = imageModelRepository
                .findImageModelByGenerationTransportId(generationTransport.getId());

        LOG.info(imageModelOptional.isPresent() + " : imageModelOptional is present, found by generationTransport");

        imageModelOptional.ifPresent(imageModelRepository::delete);

        ImageModel imageModel = new ImageModel();
        imageModel.setGenerationTransportId(generationTransport.getId());
        imageModel.setName(file.getOriginalFilename());
        imageModel.setImageBytes(compressBytes(file.getBytes()));

        imageModelRepository.save(imageModel);

    }

    @Transactional
    public void deletePostImage(Long postId, Principal principal) {
        Post post = postRepository.findPostByIdAndUser(postId, convertPrincipalToUser(principal))
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));

        ImageModel imageModel = imageModelRepository.findImageModelByPostId(post.getId())
                .orElseThrow(() -> new ImageModelNotFoundException("Generation transport image not found"));

        imageModelRepository.delete(imageModel);
    }

    @Transactional
    public void deleteAnyPostImage(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post cannot be found"));

        ImageModel imageModel = imageModelRepository.findImageModelByPostId(post.getId())
                .orElseThrow(() -> new ImageModelNotFoundException("Generation transport image not found"));

        imageModelRepository.delete(imageModel);
    }

    @Transactional
    public void deleteGenerationTransportImage(Long generationTransportId) {
        GenerationTransport generationTransport = generationTransportRepository.findById(generationTransportId)
                .orElseThrow(() -> new TransportNotFoundException("Generation transport cannot be found"));

        ImageModel imageModel = imageModelRepository.findImageModelByGenerationTransportId(generationTransport.getId())
                .orElseThrow(() -> new ImageModelNotFoundException("Generation transport image not found"));

        imageModelRepository.delete(imageModel);
    }

    public ImageModel getGenerationTransportImage(Long generationTransportId) {
        GenerationTransport generationTransport = generationTransportRepository.findById(generationTransportId)
                .orElseThrow(() -> new TransportNotFoundException("Generation transport not found"));

        ImageModel imageModel = imageModelRepository.findImageModelByGenerationTransportId(generationTransport.getId())
                .orElseThrow(() -> new ImageModelNotFoundException("Image cannot be found"));
        imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));

        return imageModel;
    }

    public ImageModel getPostImage(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        ImageModel imageModel = imageModelRepository.findImageModelByPostId(post.getId()).orElseThrow(
                () -> new ImageModelNotFoundException("Image cannot be found")
        );
        imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        return imageModel;
    }

    private User convertPrincipalToUser(Principal principal) {
        return customUserRepository.findUserByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User with email " + principal.getName() + " not found"));
    }

    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException ignored) {
        }
        return outputStream.toByteArray();
    }

    private static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException ignored) {
        }
        return outputStream.toByteArray();
    }
}
