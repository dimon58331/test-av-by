package by.av.test.testavby.controller;

import by.av.test.testavby.dto.transport.GenerationTransportDTO;
import by.av.test.testavby.dto.transport.TransportBrandDTO;
import by.av.test.testavby.dto.transport.TransportModelDTO;
import by.av.test.testavby.dto.transport.TransportParametersDTO;
import by.av.test.testavby.entity.transport.TransportParameters;
import by.av.test.testavby.mapper.transport.TransportMapper;
import by.av.test.testavby.service.TransportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/transport")
public class TransportController {
    private final TransportMapper transportMapper;
    private final TransportService transportService;
    private final Logger LOG = LoggerFactory.getLogger(TransportController.class);

    @Autowired
    public TransportController(TransportMapper transportMapper, TransportService transportService) {
        this.transportMapper = transportMapper;
        this.transportService = transportService;
    }

    @GetMapping(value = "/all/brand", params = {"size", "page"})
    public Page<TransportBrandDTO> getAllTransportBrand(@RequestParam("size") int size, @RequestParam("page") int page) {
        return transportService.getAllTransportBrandSortByAsc(size, page)
                .map(transportMapper::convertTransportBrandToTransportBrandDTO);
    }

    @GetMapping(value = "/all/model", params = {"size", "page", "transportBrandId"})
    public Page<TransportModelDTO> getAllTransportModel(@RequestParam("size") int size, @RequestParam("page") int page,
                                                        @RequestParam("transportBrandId") int transportBrandId) {
        return transportService.getAllTransportModelSortByAscAndTransportBrandId(size, page, transportBrandId)
                .map(transportMapper::convertTransportModelToTransportModelDTO);
    }

    @GetMapping(value = "/all/generation", params = {"size", "page", "releaseYear", "transportModelId"})
    public Page<GenerationTransportDTO> getAllGenerationTransport(@RequestParam("size") int size,
                                                                  @RequestParam("page") int page,
                                                                  @RequestParam("releaseYear") int releaseYear,
                                                                  @RequestParam("transportModelId") Long transportModelId) {
        return transportService.getAllGenerationTransportByReleaseYearAndTransportModelIdSortedByAsc(size, page,
                releaseYear, transportModelId).map(transportMapper::convertGenerationTransportToGenerationTransportDTO);
    }

    @GetMapping(value = "/all/generation/releaseYears", params = {"transportModelId"})
    public Map<String, Integer> getMaxAndMinGenerationTransportReleaseYearsByTransportModel(
            @RequestParam("transportModelId") Long transportModelId) {
        return transportService.getMaxAndMinGenerationTransportReleaseYearsByTransportModel(transportModelId);
    }

    @GetMapping(value = "/all/releaseYears")
    public Map<String, Integer> getMaxAndMinGenerationTransportReleaseYears() {
        return transportService.getMaxAndMinGenerationTransportReleaseYears();
    }

    @GetMapping(value = "/all/transportParameters", params = {"generationTransportId"})
    public ResponseEntity<List<TransportParametersDTO>> getAllTransportParametersByGenerationTransport(
            @RequestParam("generationTransportId") Long generationTransportId) {
        List<TransportParametersDTO> transportParametersDTOS = transportService
                .getAllTransportParametersByGenerationTransport(generationTransportId).stream()
                .map(transportMapper::convertTransportParametersToTransportParametersDTO).toList();
        return ResponseEntity.ok(transportParametersDTOS);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<TransportParametersDTO> getTransport(@PathVariable("postId") String postId) {
        TransportParameters transportParameters = transportService.getTransportParametersByPostId(Long.parseLong(postId));
        return ResponseEntity.ok(transportMapper.convertTransportParametersToTransportParametersDTO(transportParameters));
    }
}
