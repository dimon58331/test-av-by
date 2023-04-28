package by.av.test.testavby.controller;

import by.av.test.testavby.dto.transport.TransportDTO;
import by.av.test.testavby.entity.transport.Transport;
import by.av.test.testavby.service.TransportService;
import by.av.test.testavby.validator.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/api/admin")
public class AdminController {
    private final TransportService transportService;
    private final ModelMapper modelMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public AdminController(TransportService transportService, ModelMapper modelMapper, ResponseErrorValidation responseErrorValidation) {
        this.transportService = transportService;
        this.modelMapper = modelMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/transport/create")
    public ResponseEntity<Object> createTransport(@Valid @RequestBody TransportDTO transportDTO, BindingResult result){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (Objects.nonNull(errors)) return errors;

        Transport createdTransport = transportService.createTransport(convertTransportDTOToTransport(transportDTO));

        return ResponseEntity.ok(convertTransportToTransportDTO(createdTransport));
    }

    private TransportDTO convertTransportToTransportDTO(Transport transport) {
        return modelMapper.map(transport, TransportDTO.class);
    }

    private Transport convertTransportDTOToTransport(TransportDTO transportDTO){
        return modelMapper.map(transportDTO, Transport.class);
    }
}
