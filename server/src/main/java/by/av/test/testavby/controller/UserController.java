package by.av.test.testavby.controller;

import by.av.test.testavby.dto.UserDTO;
import by.av.test.testavby.entity.User;
import by.av.test.testavby.mapper.user.UserMapper;
import by.av.test.testavby.payload.response.JWTSuccessResponse;
import by.av.test.testavby.payload.response.MessageResponse;
import by.av.test.testavby.service.UserService;
import by.av.test.testavby.util.JWTUtil;
import by.av.test.testavby.validator.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final ResponseErrorValidation responseErrorValidation;
    private final JWTUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper,
                          ResponseErrorValidation responseErrorValidation, JWTUtil jwtUtil) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.responseErrorValidation = responseErrorValidation;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        return ResponseEntity.ok(userMapper.convertUserToUserDTO(userService.getCurrentUserByPrincipal(principal)));
    }

    @PatchMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult result,
                                             Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (Objects.nonNull(errors)) return errors;

        User updatedUser = userService.updateByUserAndPrincipal(userMapper.convertUserDTOToUser(userDTO), principal);

        return updatedUser.getEmail().equals(principal.getName())
                ? ResponseEntity.ok(userMapper.convertUserToUserDTO(updatedUser))
                : ResponseEntity.ok(new JWTSuccessResponse(jwtUtil.generateToken(updatedUser.getEmail())));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteCurrentUser(Principal principal) {
        userService.deleteCurrentUser(principal);
        return ResponseEntity.ok(new MessageResponse("User deleted successfully"));
    }
}
