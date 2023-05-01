package by.av.test.testavby.controller;

import by.av.test.testavby.dto.transport.TransportDTO;
import by.av.test.testavby.entity.transport.Transport;
import by.av.test.testavby.service.TransportService;
import by.av.test.testavby.validator.ResponseErrorValidation;
import org.modelmapper.ModelMapper;
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

    @PostMapping("/{postId}/{transportId}/create")
    public ResponseEntity<Object> addTransportToPost(@PathVariable("transportId") String transportId,
                                                     @PathVariable("postId") String postId) {
        Transport createdTransport = transportService
                .addTransportForPost(Long.parseLong(postId), Long.parseLong(transportId));

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
