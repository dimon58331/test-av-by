package by.av.test.testavby.controller;

import by.av.test.testavby.dto.UserDTO;
import by.av.test.testavby.entity.User;
import by.av.test.testavby.payload.response.MessageResponse;
import by.av.test.testavby.service.UserService;
import by.av.test.testavby.validator.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper,
                          ResponseErrorValidation responseErrorValidation) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal){
        return ResponseEntity.ok(convertUserToUserDTO(userService.getCurrentUserByPrincipal(principal)));
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult result,
                                             Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (Objects.nonNull(errors)) return errors;

        User updatedUser = userService.updateByUserAndPrincipal(convertUserDTOToUser(userDTO), principal);

        return ResponseEntity.ok(convertUserToUserDTO(updatedUser));
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> deleteCurrentUser(Principal principal){
        userService.deleteCurrentUser(principal);
        return ResponseEntity.ok(new MessageResponse("User deleted successfully"));
    }

    private User convertUserDTOToUser(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertUserToUserDTO(User user){
        return modelMapper.map(user, UserDTO.class);
    }
}
