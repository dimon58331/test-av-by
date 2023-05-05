package by.av.test.testavby.service;

import by.av.test.testavby.entity.Post;
import by.av.test.testavby.entity.transport.Transport;
import by.av.test.testavby.entity.transport.TransportBrand;
import by.av.test.testavby.entity.transport.TransportModel;
import by.av.test.testavby.entity.transport.TransportType;
import by.av.test.testavby.enums.ETypeEngine;
import by.av.test.testavby.exception.PostNotFoundException;
import by.av.test.testavby.exception.TransportExistsException;
import by.av.test.testavby.exception.TransportNotFoundException;
import by.av.test.testavby.repository.PostRepository;
import by.av.test.testavby.repository.transport.TransportBrandRepository;
import by.av.test.testavby.repository.transport.TransportModelRepository;
import by.av.test.testavby.repository.transport.TransportRepository;
import by.av.test.testavby.repository.transport.TransportTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TransportService {
    private final TransportRepository transportRepository;
    private final TransportBrandRepository transportBrandRepository;
    private final TransportTypeRepository transportTypeRepository;
    private final TransportModelRepository transportModelRepository;
    private final PostRepository postRepository;
    private final Logger LOG = LoggerFactory.getLogger(TransportService.class);

    @Autowired
    public TransportService(TransportRepository transportRepository, TransportBrandRepository transportBrandRepository,
                            TransportTypeRepository transportTypeRepository,
                            TransportModelRepository transportModelRepository, PostRepository postRepository) {
        this.transportRepository = transportRepository;
        this.transportBrandRepository = transportBrandRepository;
        this.transportTypeRepository = transportTypeRepository;
        this.transportModelRepository = transportModelRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public Transport addTransportForPost(Long postId, Long transportId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        if (Objects.nonNull(post.getTransport())){
            throw new TransportExistsException("Transport for this post already exists");
        }
        Transport transport = transportRepository.findById(transportId)
                .orElseThrow(() -> new TransportNotFoundException("Transport cannot be found"));
        post.setTransport(transport);
        return transportRepository.save(transport);
    }

    @Transactional
    public Transport createTransport(Transport transport, ETypeEngine eTypeEngine){
        transport.setETypeEngine(eTypeEngine);

        LOG.info(transport.getETypeEngine().name());

        Optional<TransportType> transportType = transportTypeRepository.findByTypeName(transport.getTransportModel()
                .getTransportType().getTypeName());
        Optional<TransportBrand> transportBrand = transportBrandRepository.findByBrandName(transport.getTransportModel()
                .getTransportBrand().getBrandName());

        if (transportBrand.isPresent() && transportType.isPresent()){
            Optional<TransportModel> transportModel = transportModelRepository.findByTransportBrandAndTransportType(
                    transportBrand.get(), transportType.get()
            );
            transportModel.ifPresentOrElse(transport::setTransportModel, () -> {
               throw new TransportNotFoundException("Transport model with this parameters not found!");
            });
        } else {
            if (transportBrand.isEmpty() && transportType.isEmpty()){
                throw new TransportNotFoundException("Transport with this brand and type doesnt exist");
            }
            else if (transportBrand.isEmpty()){
                throw new TransportNotFoundException("Transport with this brand doesnt exist");
            } else {
                throw new TransportNotFoundException("Transport with this type doesnt exist");
            }
        }
        try{
            return transportRepository.save(transport);
        } catch (Exception e){
            throw new TransportExistsException("Transport with these parameters already exist");
        }

    }

    @Transactional
    public TransportModel createTransportModel(TransportModel transportModel){
        Optional<TransportType> transportType = transportTypeRepository.findByTypeName(transportModel.getTransportType()
                .getTypeName());
        Optional<TransportBrand> transportBrand = transportBrandRepository.findByBrandName(transportModel
                .getTransportBrand().getBrandName());

        if (transportBrand.isPresent() && transportType.isPresent()){
            Optional<TransportModel> transportModelFromDB = transportModelRepository
                    .findByTransportBrandAndTransportType(transportBrand.get(), transportType.get());
            transportModelFromDB.ifPresentOrElse(transportModel1 -> {
                throw new TransportExistsException("Transport model with this parameters already exists");
            }, () -> {
                transportModel.setTransportBrand(transportBrand.get());
                transportModel.setTransportType(transportType.get());
            });
        } else {
            transportType.ifPresentOrElse(transportModel::setTransportType, () -> {
                transportModel.setTransportType(transportTypeRepository.save(transportModel.getTransportType()));
            });
            transportBrand.ifPresentOrElse(transportModel::setTransportBrand, () -> {
                transportModel.setTransportBrand(transportBrandRepository.save(transportModel.getTransportBrand()));
            });
        }
        return transportModelRepository.save(transportModel);
    }

    @Transactional
    public void deleteTransportById(Long transportId){
        transportRepository.deleteById(transportId);
    }

    public Page<Transport> getAllTransportSortByBrand(int size, int page){
        return transportRepository.findAllTransportSortedByBrandAsc(PageRequest.of(page, size));
    }

    public Transport getTransportByPostId(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        return transportRepository.findTransportByPost(post).orElseThrow(
                () -> new TransportNotFoundException("Transport cannot be found")
        );
    }
}
