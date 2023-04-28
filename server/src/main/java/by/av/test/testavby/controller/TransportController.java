package by.av.test.testavby.controller;

import by.av.test.testavby.dto.TransportDTO;
import by.av.test.testavby.entity.transport.Transport;
import by.av.test.testavby.service.TransportService;
import by.av.test.testavby.validator.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/api/transport")
public class TransportController {
    private final ModelMapper modelMapper;
    private final TransportService transportService;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public TransportController(ModelMapper modelMapper, TransportService transportService, ResponseErrorValidation responseErrorValidation) {
        this.modelMapper = modelMapper;
        this.transportService = transportService;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping(value = "/all", params = {"size", "page"})
    public Page<TransportDTO> getAllTransport(@RequestParam("size") int size, @RequestParam("page") int page){
        return transportService.getAllTransportSortByBrand(size, page).map(this::convertTransportToTransportDTO);
    }

    @PostMapping("/{postId}/create")
    public ResponseEntity<Object> createTransport(@Valid @RequestBody TransportDTO transportDTO, BindingResult result,
                                                  @PathVariable("postId") String postId) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (Objects.nonNull(errors)) return errors;

        Transport createdTransport = transportService.createTransportForPost(Long.parseLong(postId),
                convertTransportDTOToTransport(transportDTO));

        return ResponseEntity.ok(convertTransportToTransportDTO(createdTransport));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<TransportDTO> getTransport(@PathVariable("postId") String postId){
        Transport transport = transportService.getTransportByPostId(Long.parseLong(postId));
        return ResponseEntity.ok(convertTransportToTransportDTO(transport));
    }

    private Transport convertTransportDTOToTransport(TransportDTO transportDTO) {
        return modelMapper.map(transportDTO, Transport.class);
    }

    private TransportDTO convertTransportToTransportDTO(Transport transport){
        return modelMapper.map(transport, TransportDTO.class);
    }
}
