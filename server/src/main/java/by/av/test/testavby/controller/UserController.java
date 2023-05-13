package by.av.test.testavby.controller;

import by.av.test.testavby.dto.UserDTO;
import by.av.test.testavby.entity.User;
import by.av.test.testavby.mapper.user.UserMapper;
import by.av.test.testavby.payload.response.MessageResponse;
import by.av.test.testavby.service.UserService;
import by.av.test.testavby.validator.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final UserMapper userMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper,
                          ResponseErrorValidation responseErrorValidation) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        return ResponseEntity.ok(userMapper.convertUserToUserDTO(userService.getCurrentUserByPrincipal(principal)));
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult result,
                                             Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (Objects.nonNull(errors)) return errors;

        User updatedUser = userService.updateByUserAndPrincipal(userMapper.convertUserDTOToUser(userDTO), principal);

        return ResponseEntity.ok(userMapper.convertUserToUserDTO(updatedUser));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteCurrentUser(Principal principal) {
        userService.deleteCurrentUser(principal);
        return ResponseEntity.ok(new MessageResponse("User deleted successfully"));
    }
}
