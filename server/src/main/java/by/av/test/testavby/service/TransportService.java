package by.av.test.testavby.service;

import by.av.test.testavby.entity.Post;
import by.av.test.testavby.entity.transport.GenerationTransport;
import by.av.test.testavby.entity.transport.TransportBrand;
import by.av.test.testavby.entity.transport.TransportModel;
import by.av.test.testavby.entity.transport.TransportParameters;
import by.av.test.testavby.exception.PostNotFoundException;
import by.av.test.testavby.exception.TransportExistsException;
import by.av.test.testavby.exception.TransportNotFoundException;
import by.av.test.testavby.repository.PostRepository;
import by.av.test.testavby.repository.transport.GenerationTransportRepository;
import by.av.test.testavby.repository.transport.TransportBrandRepository;
import by.av.test.testavby.repository.transport.TransportModelRepository;
import by.av.test.testavby.repository.transport.TransportParametersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class TransportService {
    private final TransportParametersRepository transportParametersRepository;
    private final TransportBrandRepository transportBrandRepository;
    private final TransportModelRepository transportModelRepository;
    private final GenerationTransportRepository generationTransportRepository;
    private final PostRepository postRepository;
    private final Logger LOG = LoggerFactory.getLogger(TransportService.class);

    @Autowired
    public TransportService(TransportParametersRepository transportParametersRepository,
                            TransportBrandRepository transportBrandRepository,
                            TransportModelRepository transportModelRepository, GenerationTransportRepository generationTransportRepository, PostRepository postRepository) {
        this.transportParametersRepository = transportParametersRepository;
        this.transportBrandRepository = transportBrandRepository;
        this.transportModelRepository = transportModelRepository;
        this.generationTransportRepository = generationTransportRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public TransportParameters addTransportToPost(Long postId, Long transportParametersId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        if (Objects.nonNull(post.getTransportParameters())) {
            throw new TransportExistsException("Transport for this post already exists");
        }
        TransportParameters transportParameters = transportParametersRepository.findById(transportParametersId)
                .orElseThrow(() -> new TransportNotFoundException("Transport cannot be found"));
        post.setTransportParameters(transportParameters);
        return transportParametersRepository.save(transportParameters);
    }

    @Transactional
    public TransportBrand createTransportBrand(TransportBrand transportBrand) {
        try {
            return transportBrandRepository.save(transportBrand);
        } catch (Exception e) {
            throw new TransportExistsException("Transport with this brand already exists");
        }
    }

    @Transactional
    public TransportModel createTransportModelForTransportBrand(TransportModel transportModel, int transportBrandId) {
        TransportBrand transportBrand = transportBrandRepository.findById(transportBrandId)
                .orElseThrow(() -> new TransportNotFoundException("Transport brand with this id not found"));
        transportModel.setTransportBrand(transportBrand);
        try {
            return transportModelRepository.save(transportModel);
        } catch (Exception e) {
            throw new TransportExistsException("Transport model for this brand already exists");
        }
    }

    @Transactional
    public GenerationTransport createGenerationTransportForTransportModel(GenerationTransport generationTransport,
                                                                          Long transportModelId) {
        TransportModel transportModel = transportModelRepository.findById(transportModelId)
                .orElseThrow(() -> new TransportNotFoundException("Transport model with this id not found"));
        generationTransport.setTransportModel(transportModel);
        try {
            return generationTransportRepository.save(generationTransport);
        } catch (Exception e) {
            throw new TransportExistsException("Generation for this transport model already exists");
        }
    }

    @Transactional
    public TransportParameters createTransportParametersForGenerationTransport(TransportParameters transportParameters,
                                                                               Long generationTransportId) {
        GenerationTransport generationTransport = generationTransportRepository.findById(generationTransportId)
                .orElseThrow(() -> new TransportNotFoundException("Generation of transport with this id not found"));
        transportParameters.setGenerationTransport(generationTransport);
        try {
            return transportParametersRepository.save(transportParameters);
        } catch (Exception e) {
            throw new TransportExistsException("Parameters for generation of this transport already exists");
        }
    }

    @Transactional
    public void deleteTransportById(Long transportId) {
        transportParametersRepository.deleteById(transportId);
    }

    public Page<TransportBrand> getAllTransportBrandSortByAsc(int size, int page) {
        return transportBrandRepository.findAll(PageRequest.of(page, size, Sort.by("brandName")));
    }

    public Page<TransportModel> getAllTransportModelSortByAscAndTransportBrandId(int size, int page,
                                                                                 int transportBrandId) {
        return transportModelRepository.findAllByTransportBrand(transportBrandRepository.findById(transportBrandId)
                        .orElseThrow(() -> new TransportNotFoundException("Transport brand with this id not found")),
                PageRequest.of(page, size, Sort.by("modelName")));
    }

    public Page<GenerationTransport> getAllGenerationTransportByReleaseYearAndTransportModelIdSortedByAsc(int size
            , int page, int releaseYear, Long transportModelId) {
        Page<GenerationTransport> generationTransports = generationTransportRepository
                .findAllByTransportModelAndEndReleaseYearGreaterThanEqualAndStartReleaseYearLessThanEqual(
                        transportModelRepository.findById(transportModelId).orElseThrow(
                                () -> new TransportNotFoundException("Transport model with this id not found")
                        ), releaseYear, releaseYear, PageRequest.of(page, size, Sort.by("generationName"))
                );
        if (generationTransports.getContent().isEmpty()) {
            throw new TransportNotFoundException("Transport with these parameters not found");
        }
        return generationTransports;
    }

    public List<TransportParameters> getAllTransportParametersByGenerationTransport(Long generationTransportId) {
        GenerationTransport generationTransport = generationTransportRepository.findById(generationTransportId)
                .orElseThrow(() -> new TransportNotFoundException("Generation of this transport not found"));
        return transportParametersRepository.findAllByGenerationTransportOrderByEnginePower(generationTransport);
    }

    public Map<String, Integer> getMaxAndMinGenerationTransportReleaseYearsByTransportModel(Long transportModelId) {
        TransportModel transportModel = transportModelRepository.findById(transportModelId)
                .orElseThrow(() -> new TransportNotFoundException("Transport model with this id not found"));

        int minStartReleaseYear = generationTransportRepository
                .findAllByTransportModelOrderByStartReleaseYear(transportModel).stream().findFirst().orElseThrow(
                        () -> new TransportNotFoundException("Generation of this transport not found")
                ).getStartReleaseYear();
        int maxEndReleaseYear = generationTransportRepository
                .findAllByTransportModelOrderByEndReleaseYearDesc(transportModel).stream().findFirst().orElseThrow(
                        () -> new TransportNotFoundException("Generation of this transport not found")
                ).getEndReleaseYear();

        Map<String, Integer> generationTransportReleaseYears = new HashMap<>();
        generationTransportReleaseYears.put("minStartReleaseYear", minStartReleaseYear);
        generationTransportReleaseYears.put("maxEndReleaseYear", maxEndReleaseYear);

        return generationTransportReleaseYears;
    }

    public Map<String, Integer> getMaxAndMinGenerationTransportReleaseYears() {
        int minStartReleaseYear = generationTransportRepository
                .findAllByOrderByStartReleaseYear().stream().findFirst().orElseThrow(
                        () -> new TransportNotFoundException("Generation of this transport not found")
                ).getStartReleaseYear();

        int maxEndReleaseYear = generationTransportRepository
                .findAllByOrderByEndReleaseYearDesc().stream().findFirst().orElseThrow(
                        () -> new TransportNotFoundException("Generation of this transport not found")
                ).getEndReleaseYear();

        Map<String, Integer> generationTransportReleaseYears = new HashMap<>();
        generationTransportReleaseYears.put("minStartReleaseYear", minStartReleaseYear);
        generationTransportReleaseYears.put("maxEndReleaseYear", maxEndReleaseYear);

        return generationTransportReleaseYears;
    }

    public TransportParameters getTransportParametersByPostId(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        return transportParametersRepository.findTransportParametersByPost(post)
                .orElseThrow(() -> new TransportNotFoundException("Transport cannot be found"));
    }
}
