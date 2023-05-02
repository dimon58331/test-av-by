package by.av.test.testavby.controller;

import by.av.test.testavby.dto.UserDTO;
import by.av.test.testavby.dto.transport.TransportDTO;
import by.av.test.testavby.entity.User;
import by.av.test.testavby.entity.transport.Transport;
import by.av.test.testavby.enums.ETypeEngine;
import by.av.test.testavby.payload.response.MessageResponse;
import by.av.test.testavby.service.TransportService;
import by.av.test.testavby.service.UserService;
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
@RequestMapping("/api/admin")
public class AdminController {
    private final TransportService transportService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public AdminController(TransportService transportService, UserService userService, ModelMapper modelMapper,
                           ResponseErrorValidation responseErrorValidation) {
        this.transportService = transportService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/transport/create")
    public ResponseEntity<Object> createTransport(@Valid @RequestBody TransportDTO transportDTO,
                                                  @RequestParam("engineType") ETypeEngine eTypeEngine,
                                                  BindingResult result){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (Objects.nonNull(errors)) return errors;

        Transport createdTransport = transportService
                .createTransport(convertTransportDTOToTransport(transportDTO), eTypeEngine);

        return ResponseEntity.ok(convertTransportToTransportDTO(createdTransport));
    }
    @PostMapping("/transport/{transportId}/delete")
    public ResponseEntity<MessageResponse> deleteTransportById(@PathVariable("transportId") String transportId){
        transportService.deleteTransportById(Long.parseLong(transportId));

        return ResponseEntity.ok(new MessageResponse("Transport deleted successfully"));
    }

    @GetMapping(value = "/users", params = {"page", "size"})
    public ResponseEntity<Page<UserDTO>> getAllUsers(@RequestParam("page") int page, @RequestParam("size") int size){
        return ResponseEntity.ok(userService.findAllSortedByNameAndSurname(page, size).map(this::convertUserToUserDTO));
    }

    private TransportDTO convertTransportToTransportDTO(Transport transport) {
        return modelMapper.map(transport, TransportDTO.class);
    }

    private Transport convertTransportDTOToTransport(TransportDTO transportDTO){
        return modelMapper.map(transportDTO, Transport.class);
    }

    private User convertUserDTOToUser(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertUserToUserDTO(User user){
        return modelMapper.map(user, UserDTO.class);
    }

}
