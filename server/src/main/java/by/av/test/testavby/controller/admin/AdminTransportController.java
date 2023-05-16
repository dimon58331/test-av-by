package by.av.test.testavby.controller.admin;

import by.av.test.testavby.dto.transport.GenerationTransportDTO;
import by.av.test.testavby.dto.transport.TransportBrandDTO;
import by.av.test.testavby.dto.transport.TransportModelDTO;
import by.av.test.testavby.dto.transport.TransportParametersDTO;
import by.av.test.testavby.entity.transport.GenerationTransport;
import by.av.test.testavby.entity.transport.TransportBrand;
import by.av.test.testavby.entity.transport.TransportModel;
import by.av.test.testavby.entity.transport.TransportParameters;
import by.av.test.testavby.enums.EBodyType;
import by.av.test.testavby.enums.ETransmissionType;
import by.av.test.testavby.enums.ETypeEngine;
import by.av.test.testavby.mapper.transport.TransportMapper;
import by.av.test.testavby.payload.response.MessageResponse;
import by.av.test.testavby.service.TransportService;
import by.av.test.testavby.validator.ResponseErrorValidation;
import jakarta.validation.Valid;
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
    private final TransportMapper transportMapper;
    private final ResponseErrorValidation responseErrorValidation;
    private final Logger LOG = LoggerFactory.getLogger(AdminTransportController.class);

    @Autowired
    public AdminTransportController(TransportService transportService, TransportMapper transportMapper,
                                    ResponseErrorValidation responseErrorValidation) {
        this.transportService = transportService;
        this.transportMapper = transportMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/brand/create")
    public ResponseEntity<Object> createTransportBrand(@Valid @RequestBody TransportBrandDTO transportBrandDTO,
                                                       BindingResult result) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (Objects.nonNull(errors)) return errors;

        LOG.info(transportBrandDTO.toString());

        TransportBrand createdTransportBrand = transportService
                .createTransportBrand(transportMapper.convertTransportBrandDTOToTransportBrand(transportBrandDTO));

        return ResponseEntity.ok(transportMapper.convertTransportBrandToTransportBrandDTO(createdTransportBrand));
    }

    @PostMapping(value = "/model/create", params = {"transportBrandId"})
    public ResponseEntity<Object> createTransportModel(@Valid @RequestBody TransportModelDTO transportModelDTO,
                                                       BindingResult result,
                                                       @RequestParam("transportBrandId") int transportBrandId) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (Objects.nonNull(errors)) return errors;

        LOG.info(transportModelDTO.toString());

        TransportModel createdTransportModel = transportService.createTransportModelForTransportBrand(
                transportMapper.convertTransportModelDTOToTransportModel(transportModelDTO), transportBrandId);

        return ResponseEntity.ok(transportMapper.convertTransportModelToTransportModelDTO(createdTransportModel));
    }

    @PostMapping(value = "/generation/create", params = {"transportModelId"})
    public ResponseEntity<Object> createGenerationTransport(
            @Valid @RequestBody GenerationTransportDTO generationTransportDTO, BindingResult result,
            @RequestParam("transportModelId") Long transportModelId) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (Objects.nonNull(errors)) return errors;

        LOG.info(generationTransportDTO.toString());

        GenerationTransport createdGenerationTransport = transportService.createGenerationTransportForTransportModel(
                transportMapper.convertGenerationTransportDTOToGenerationTransport(generationTransportDTO), transportModelId);

        return ResponseEntity.ok(transportMapper.convertGenerationTransportToGenerationTransportDTO(createdGenerationTransport));
    }

    @PostMapping(value = "/parameters/create", params = {"generationTransportId", "bodyType", "engineType", "transmissionType"})
    public ResponseEntity<Object> createTransportParameters(
            @Valid @RequestBody TransportParametersDTO transportParametersDTO, BindingResult result,
            @RequestParam("generationTransportId") Long generationTransportId, @RequestParam("bodyType") EBodyType eBodyType,
            @RequestParam("engineType") ETypeEngine eTypeEngine,
            @RequestParam("transmissionType") ETransmissionType eTransmissionType) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (Objects.nonNull(errors)) return errors;

        LOG.info(transportParametersDTO.toString());

        TransportParameters createdTransportParameters = transportService.createTransportParametersForGenerationTransport(
                transportMapper.convertTransportParametersDTOToTransportParameters(transportParametersDTO),
                generationTransportId, eBodyType, eTypeEngine, eTransmissionType);

        return ResponseEntity.ok(transportMapper.convertTransportParametersToTransportParametersDTO(createdTransportParameters));
    }

    @DeleteMapping("/parameters/delete/{id}")
    public ResponseEntity<MessageResponse> deleteTransportParametersById(@PathVariable("id") String transportParametersId) {
        transportService.deleteTransportParametersById(Long.parseLong(transportParametersId));

        return ResponseEntity.ok(new MessageResponse("Transport parameters deleted successfully"));
    }

    @DeleteMapping("/generation/delete/{id}")
    public ResponseEntity<MessageResponse> deleteGenerationTransportById(@PathVariable("id") String generationTransportId) {
        transportService.deleteGenerationTransportById(Long.parseLong(generationTransportId));

        return ResponseEntity.ok(new MessageResponse("Generation of this transport deleted successfully"));
    }

    @DeleteMapping("/model/delete/{id}")
    public ResponseEntity<MessageResponse> deleteTransportModelById(@PathVariable("id") String transportModelId) {
        transportService.deleteTransportModelById(Long.parseLong(transportModelId));

        return ResponseEntity.ok(new MessageResponse("Transport model deleted successfully"));
    }

    @DeleteMapping("/brand/delete/{id}")
    public ResponseEntity<MessageResponse> deleteTransportBrandById(@PathVariable("id") String transportBrandId) {
        transportService.deleteTransportBrandById(Integer.parseInt(transportBrandId));

        return ResponseEntity.ok(new MessageResponse("Transport brand deleted successfully"));
    }
}
