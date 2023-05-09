package by.av.test.testavby.service;

import by.av.test.testavby.entity.Post;
import by.av.test.testavby.entity.transport.TransportBrand;
import by.av.test.testavby.entity.transport.TransportParameters;
import by.av.test.testavby.exception.PostNotFoundException;
import by.av.test.testavby.exception.TransportExistsException;
import by.av.test.testavby.exception.TransportNotFoundException;
import by.av.test.testavby.repository.PostRepository;
import by.av.test.testavby.repository.transport.TransportBrandRepository;
import by.av.test.testavby.repository.transport.TransportParametersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class TransportService {
    private final TransportParametersRepository transportParametersRepository;
    private final TransportBrandRepository transportBrandRepository;
    private final PostRepository postRepository;
    private final Logger LOG = LoggerFactory.getLogger(TransportService.class);

    @Autowired
    public TransportService(TransportParametersRepository transportParametersRepository,
                            TransportBrandRepository transportBrandRepository,
                            PostRepository postRepository) {
        this.transportParametersRepository = transportParametersRepository;
        this.transportBrandRepository = transportBrandRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public TransportParameters addTransportForPost(Long postId, Long transportParametersId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        if (Objects.nonNull(post.getTransportParameters())){
            throw new TransportExistsException("Transport for this post already exists");
        }
        TransportParameters transportParameters = transportParametersRepository.findById(transportParametersId)
                .orElseThrow(() -> new TransportNotFoundException("Transport cannot be found"));
        post.setTransportParameters(transportParameters);
        return transportParametersRepository.save(transportParameters);
    }

//    @Transactional
//    public TransportModel createTransportModel(TransportModel transportModel){
//        Optional<TransportType> transportType = transportTypeRepository.findByTypeName(transportModel.getTransportType()
//                .getTypeName());
//        Optional<TransportBrand> transportBrand = transportBrandRepository.findByBrandName(transportModel
//                .getTransportBrand().getBrandName());
//
//        if (transportBrand.isPresent() && transportType.isPresent()){
//            Optional<TransportModel> transportModelFromDB = transportModelRepository
//                    .findByTransportBrandAndTransportType(transportBrand.get(), transportType.get());
//            transportModelFromDB.ifPresentOrElse(transportModel1 -> {
//                throw new TransportExistsException("Transport model with this parameters already exists");
//            }, () -> {
//                transportModel.setTransportBrand(transportBrand.get());
//                transportModel.setTransportType(transportType.get());
//            });
//        } else {
//            transportType.ifPresentOrElse(transportModel::setTransportType, () -> {
//                transportModel.setTransportType(transportTypeRepository.save(transportModel.getTransportType()));
//            });
//            transportBrand.ifPresentOrElse(transportModel::setTransportBrand, () -> {
//                transportModel.setTransportBrand(transportBrandRepository.save(transportModel.getTransportBrand()));
//            });
//        }
//        return transportModelRepository.save(transportModel);
//    }

    @Transactional
    public void deleteTransportById(Long transportId){
        transportParametersRepository.deleteById(transportId);
    }

    public Page<TransportBrand> getAllTransportBrandSortByAsc(int size, int page){
        return transportBrandRepository.findAll(PageRequest.of(page, size, Sort.by("brandName")));
    }

    public TransportParameters getTransportParametersByPostId(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        return transportParametersRepository.findTransportParametersByPost(post).orElseThrow(
                () -> new TransportNotFoundException("Transport cannot be found")
        );
    }
}
