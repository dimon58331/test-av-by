package by.av.test.testavby.controller;

import by.av.test.testavby.dto.transport.TransportBrandDTO;
import by.av.test.testavby.dto.transport.TransportParametersDTO;
import by.av.test.testavby.entity.transport.TransportBrand;
import by.av.test.testavby.entity.transport.TransportParameters;
import by.av.test.testavby.service.TransportService;
import by.av.test.testavby.validator.ResponseErrorValidation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/transport")
public class TransportController {
    private final ModelMapper modelMapper;
    private final TransportService transportService;
    private final ResponseErrorValidation responseErrorValidation;
    private final Logger LOG = LoggerFactory.getLogger(TransportController.class);

    @Autowired
    public TransportController(ModelMapper modelMapper, TransportService transportService,
                               ResponseErrorValidation responseErrorValidation) {
        this.modelMapper = modelMapper;
        this.transportService = transportService;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping(value = "/all", params = {"size", "page"})
    public Page<TransportBrandDTO> getAllTransportBrand(@RequestParam("size") int size, @RequestParam("page") int page){
        return transportService.getAllTransportBrandSortByAsc(size, page)
                .map(this::convertTransportBrandToTransportBrandDTO);
    }

    @PostMapping("/{postId}/{transportId}/create")
    public ResponseEntity<Object> addTransportToPost(@PathVariable("transportId") String transportId,
                                                     @PathVariable("postId") String postId) {
        TransportParameters createdTransportParameters = transportService
                .addTransportForPost(Long.parseLong(postId), Long.parseLong(transportId));

        return ResponseEntity.ok(convertTransportParametersToTransportParametersDTO(createdTransportParameters));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<TransportParametersDTO> getTransport(@PathVariable("postId") String postId){
        TransportParameters transportParameters = transportService.getTransportParametersByPostId(Long.parseLong(postId));
        return ResponseEntity.ok(convertTransportParametersToTransportParametersDTO(transportParameters));
    }

    private TransportParameters convertTransportParametersDTOToTransportParameters(TransportParametersDTO transportParametersDTO) {
        return modelMapper.map(transportParametersDTO, TransportParameters.class);
    }

    private TransportParametersDTO convertTransportParametersToTransportParametersDTO(TransportParameters transportParameters){
        return modelMapper.map(transportParameters, TransportParametersDTO.class);
    }

    private TransportBrandDTO convertTransportBrandToTransportBrandDTO(TransportBrand transportBrand) {
        return modelMapper.map(transportBrand, TransportBrandDTO.class);
    }
}
