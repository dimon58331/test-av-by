package by.av.test.testavby.controller.admin;

import by.av.test.testavby.dto.transport.TransportDTO;
import by.av.test.testavby.dto.transport.TransportModelDTO;
import by.av.test.testavby.entity.transport.Transport;
import by.av.test.testavby.entity.transport.TransportModel;
import by.av.test.testavby.enums.ETypeEngine;
import by.av.test.testavby.payload.response.MessageResponse;
import by.av.test.testavby.service.TransportService;
import by.av.test.testavby.validator.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/api/admin/transport")
public class AdminTransportController {
    private final TransportService transportService;
    private final ModelMapper modelMapper;
    private final ResponseErrorValidation responseErrorValidation;
    private final Logger LOG = LoggerFactory.getLogger(AdminTransportController.class);

    @Autowired
    public AdminTransportController(TransportService transportService, ModelMapper modelMapper,
                                    ResponseErrorValidation responseErrorValidation) {
        this.transportService = transportService;
        this.modelMapper = modelMapper;
        this.responseErrorValidation = responseErrorValidation;
    }
    @PostMapping("/model/create")
    public ResponseEntity<Object> createTransport(@Valid @RequestBody TransportModelDTO transportModelDTO,
                                                  BindingResult result){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (Objects.nonNull(errors)) return errors;

        LOG.info(transportModelDTO.toString());

        TransportModel createdTransport = transportService
                .createTransportModel(convertTransportModelDTOToTransportModel(transportModelDTO));

        return ResponseEntity.ok(convertTransportModelToTransportModelDTO(createdTransport));
    }
    @PostMapping("/{transportId}/delete")
    public ResponseEntity<MessageResponse> deleteTransportById(@PathVariable("transportId") String transportId){
        transportService.deleteTransportById(Long.parseLong(transportId));

        return ResponseEntity.ok(new MessageResponse("Transport deleted successfully"));
    }

    private TransportModelDTO convertTransportModelToTransportModelDTO(TransportModel transport) {
        return modelMapper.map(transport, TransportModelDTO.class);
    }

    private TransportModel convertTransportModelDTOToTransportModel(TransportModelDTO transportModelDTO){
        return modelMapper.map(transportModelDTO, TransportModel.class);
    }
}
