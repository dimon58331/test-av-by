package by.av.test.testavby.service;

import by.av.test.testavby.entity.Post;
import by.av.test.testavby.entity.transport.GenerationTransport;
import by.av.test.testavby.entity.transport.TransportBrand;
import by.av.test.testavby.entity.transport.TransportModel;
import by.av.test.testavby.entity.transport.TransportParameters;
import by.av.test.testavby.enums.EBodyType;
import by.av.test.testavby.enums.ETransmissionType;
import by.av.test.testavby.enums.ETypeEngine;
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
import org.springframework.data.domain.PageImpl;
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
                            TransportModelRepository transportModelRepository,
                            GenerationTransportRepository generationTransportRepository, PostRepository postRepository) {
        this.transportParametersRepository = transportParametersRepository;
        this.transportBrandRepository = transportBrandRepository;
        this.transportModelRepository = transportModelRepository;
        this.generationTransportRepository = generationTransportRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public TransportBrand createTransportBrand(TransportBrand transportBrand) {
        try {
            LOG.info("Before saving");
            return transportBrandRepository.save(transportBrand);
        } catch (Exception e) {
            LOG.error("Exception!");
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
                                                                               Long generationTransportId,
                                                                               EBodyType eBodyType,
                                                                               ETypeEngine eTypeEngine,
                                                                               ETransmissionType eTransmissionType) {
        GenerationTransport generationTransport = generationTransportRepository.findById(generationTransportId)
                .orElseThrow(() -> new TransportNotFoundException("Generation of transport with this id not found"));
        transportParameters.setGenerationTransport(generationTransport);
        transportParameters.setBodyType(eBodyType);
        transportParameters.setTransmissionType(eTransmissionType);
        transportParameters.setTypeEngine(eTypeEngine);
        try {
            return transportParametersRepository.save(transportParameters);
        } catch (Exception e) {
            throw new TransportExistsException("Parameters for generation of this transport already exists");
        }
    }

    @Transactional
    public void deleteTransportParametersById(Long transportId) {
        transportParametersRepository.delete(transportParametersRepository.findById(transportId)
                .orElseThrow(() -> new TransportNotFoundException("Transport parameters not found")));
    }

    @Transactional
    public void deleteTransportModelById(Long transportModelId) {
        transportModelRepository.delete(transportModelRepository.findById(transportModelId)
                .orElseThrow(() -> new TransportNotFoundException("Transport model not found")));
    }

    @Transactional
    public void deleteGenerationTransportById(Long generationTransportId) {
        generationTransportRepository.delete(generationTransportRepository.findById(generationTransportId)
                .orElseThrow(() -> new TransportNotFoundException("Generation of this transport not found")));
    }

    @Transactional
    public void deleteTransportBrandById(Integer transportBrandId) {
        transportBrandRepository.delete(transportBrandRepository.findById(transportBrandId)
                .orElseThrow(() -> new TransportNotFoundException("Transport brand not found")));
    }

    public Page<TransportParameters> getAllTransportBySomeParameters(int size, int page, EBodyType eBodyType,
                                                                ETransmissionType eTransmissionType,
                                                                ETypeEngine eTypeEngine, Integer minReleaseYear,
                                                                Integer maxReleaseYear) {
        Map<String, Integer> maxAndMinReleaseYears = getMaxAndMinTransportReleaseYears();


        LOG.info("maxAndMinReleaseYears: " + maxAndMinReleaseYears.toString());
        LOG.info("minReleaseYear: " + maxAndMinReleaseYears.get("minReleaseYear"));
        LOG.info("maxReleaseYear: " + maxAndMinReleaseYears.get("maxReleaseYear"));

        List<TransportParameters> transportParameters = transportParametersRepository.findAll();

        if (Objects.nonNull(minReleaseYear) || Objects.nonNull(maxReleaseYear)) {
            try {
                List<TransportParameters> transportParametersList = transportParametersRepository
                        .findAllByReleaseYearBetween(
                                Objects.nonNull(minReleaseYear) ? minReleaseYear
                                        : maxAndMinReleaseYears.get("minReleaseYear"),
                                Objects.nonNull(maxReleaseYear) ? maxReleaseYear
                                        : maxAndMinReleaseYears.get("maxReleaseYear")
                        );
                transportParameters = getTransportParameters(transportParameters, transportParametersList);
            } catch (Exception e) {
                throw new TransportNotFoundException("Transport parameters not found");
            }
        }
        if (Objects.nonNull(eBodyType)) {
            try {
                List<TransportParameters> transportParametersList = transportParametersRepository.findAll().stream()
                        .filter(transportParameters1 -> transportParameters1.getBodyType().equals(eBodyType)).toList();
                transportParameters = getTransportParameters(transportParameters, transportParametersList);
            } catch (Exception e) {
                throw new TransportNotFoundException("Transport parameters not found");
            }
        }
        if (Objects.nonNull(eTransmissionType)) {
            try {
                List<TransportParameters> transportParametersList = transportParametersRepository.findAll().stream()
                        .filter(transportParameters1 -> transportParameters1.getTransmissionType().equals(eTransmissionType)).toList();
                transportParameters = getTransportParameters(transportParameters, transportParametersList);
            } catch (Exception e) {
                throw new TransportNotFoundException("Transport parameters not found");
            }
        }
        if (Objects.nonNull(eTypeEngine)) {
            try {
                List<TransportParameters> transportParametersList = transportParametersRepository.findAll().stream()
                        .filter(transportParameters1 -> transportParameters1.getTypeEngine().equals(eTypeEngine)).toList();
                transportParameters = getTransportParameters(transportParameters, transportParametersList);
            } catch (Exception e) {
                throw new TransportNotFoundException("Transport parameters not found");
            }
        }

        return new PageImpl<>(transportParameters, PageRequest.of(page, size), transportParameters.size());
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

    public List<GenerationTransport> getAllGenerationTransportsByTransportModelIdSortedByReleaseYear(Long transportModelId) {
        List<GenerationTransport> generationTransports = generationTransportRepository
                .findAllByTransportModelOrderByStartReleaseYear(transportModelRepository.findById(transportModelId)
                        .orElseThrow(() -> new TransportNotFoundException("Transport model with this id not found")));
        if (generationTransports.isEmpty()) {
            throw new TransportNotFoundException("Transport with these parameters not found");
        }
        return generationTransports;
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
        generationTransportReleaseYears.put("maxStartReleaseYear", minStartReleaseYear);
        generationTransportReleaseYears.put("minEndReleaseYear", maxEndReleaseYear);

        return generationTransportReleaseYears;
    }

    public Map<String, Integer> getMaxAndMinTransportReleaseYears() {
        int minReleaseYear = transportParametersRepository
                .findFirstByOrderByReleaseYear().orElseThrow(
                        () -> new TransportNotFoundException("Transport parameters not exist")
                ).getReleaseYear();

        int maxReleaseYear = transportParametersRepository
                .findFirstByOrderByReleaseYearDesc().orElseThrow(
                        () -> new TransportNotFoundException("Transport parameters not exist")
                ).getReleaseYear();

        Map<String, Integer> generationTransportReleaseYears = new HashMap<>();
        generationTransportReleaseYears.put("minReleaseYear", minReleaseYear);
        generationTransportReleaseYears.put("maxReleaseYear", maxReleaseYear);

        return generationTransportReleaseYears;
    }

    public TransportParameters getTransportParametersByPostId(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        return transportParametersRepository.findTransportParametersByPost(post)
                .orElseThrow(() -> new TransportNotFoundException("Transport cannot be found"));
    }

    private List<TransportParameters> getTransportParameters(List<TransportParameters> transportParameters,
                                                             List<TransportParameters> transportParametersList)
            throws Exception {
        if (transportParameters.isEmpty()) {
            throw new Exception();
        }

        List<TransportParameters> equalsTransportParameters = new ArrayList<>();
        for (TransportParameters transportParameters1 : transportParametersList) {
            equalsTransportParameters.addAll(transportParameters.stream().filter(
                    transportParameters2 -> transportParameters2.getId().equals(transportParameters1.getId())).toList()
            );
        }
        if (!equalsTransportParameters.isEmpty()) {
            transportParameters = equalsTransportParameters;
        }
        return transportParameters;
    }
}
