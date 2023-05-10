package by.av.test.testavby.mapper.transport;

import by.av.test.testavby.dto.transport.GenerationTransportDTO;
import by.av.test.testavby.dto.transport.TransportBrandDTO;
import by.av.test.testavby.dto.transport.TransportModelDTO;
import by.av.test.testavby.dto.transport.TransportParametersDTO;
import by.av.test.testavby.entity.transport.GenerationTransport;
import by.av.test.testavby.entity.transport.TransportBrand;
import by.av.test.testavby.entity.transport.TransportModel;
import by.av.test.testavby.entity.transport.TransportParameters;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransportMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public TransportMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TransportParameters convertTransportParametersDTOToTransportParameters(TransportParametersDTO parameters) {
        return modelMapper.map(parameters, TransportParameters.class);
    }

    public TransportParametersDTO convertTransportParametersToTransportParametersDTO(TransportParameters parameters) {
        return modelMapper.map(parameters, TransportParametersDTO.class);
    }

    public TransportBrandDTO convertTransportBrandToTransportBrandDTO(TransportBrand transportBrand) {
        return modelMapper.map(transportBrand, TransportBrandDTO.class);
    }

    public TransportBrand convertTransportBrandDTOToTransportBrand(TransportBrandDTO transportBrandDTO) {
        return modelMapper.map(transportBrandDTO, TransportBrand.class);
    }

    public TransportModel convertTransportModelDTOToTransportModel(TransportModelDTO transportModelDTO) {
        return modelMapper.map(transportModelDTO, TransportModel.class);
    }

    public TransportModelDTO convertTransportModelToTransportModelDTO(TransportModel transportModel) {
        return modelMapper.map(transportModel, TransportModelDTO.class);
    }

    public GenerationTransportDTO convertGenerationTransportToGenerationTransportDTO(GenerationTransport generation) {
        return modelMapper.map(generation, GenerationTransportDTO.class);
    }

    public GenerationTransport convertGenerationTransportDTOToGenerationTransport(GenerationTransportDTO generationDTO) {
        return modelMapper.map(generationDTO, GenerationTransport.class);
    }
}
