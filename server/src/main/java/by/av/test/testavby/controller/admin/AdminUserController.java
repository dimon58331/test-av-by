package by.av.test.testavby.controller.admin;

import by.av.test.testavby.dto.UserDTO;
import by.av.test.testavby.entity.User;
import by.av.test.testavby.mapper.user.UserMapper;
import by.av.test.testavby.payload.response.MessageResponse;
import by.av.test.testavby.service.UserService;
import by.av.test.testavby.validator.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/api/admin/user")
public class AdminUserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public AdminUserController(UserService userService, UserMapper userMapper,
                               ResponseErrorValidation responseErrorValidation) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping(value = "/all", params = {"page", "size"})
    public ResponseEntity<Page<UserDTO>> getAllUsers(@RequestParam("page") int page, @RequestParam("size") int size) {
        return ResponseEntity.ok(userService.findAllSortedByNameAndSurname(page, size)
                .map(userMapper::convertUserToUserDTO));
    }

    @PostMapping("/{userId}/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult result,
                                             @PathVariable("userId") String userId) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (Objects.nonNull(errors)) return errors;

        User updatedUser = userService.updateByUserAndUserId(userMapper.convertUserDTOToUser(userDTO),
                Long.parseLong(userId));

        return ResponseEntity.ok(userMapper.convertUserToUserDTO(updatedUser));
    }

    @PostMapping("/{userId}/delete")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUserById(Long.parseLong(userId));
        return ResponseEntity.ok(new MessageResponse("Deleted successfully"));
    }
}
