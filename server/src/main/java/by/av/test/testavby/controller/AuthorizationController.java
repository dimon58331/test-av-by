package by.av.test.testavby.controller;

import by.av.test.testavby.entity.User;
import by.av.test.testavby.payload.request.AuthenticationRequest;
import by.av.test.testavby.payload.request.RegistrationRequest;
import by.av.test.testavby.payload.response.JWTSuccessResponse;
import by.av.test.testavby.payload.response.MessageResponse;
import by.av.test.testavby.validator.ResponseErrorValidation;
import by.av.test.testavby.service.UserService;
import by.av.test.testavby.util.JWTUtil;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthorizationController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final ResponseErrorValidation responseErrorValidation;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthorizationController(UserService userService, AuthenticationManager authenticationManager, JWTUtil jwtUtil,
                                   ResponseErrorValidation responseErrorValidation, ModelMapper modelMapper) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.responseErrorValidation = responseErrorValidation;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody AuthenticationRequest authenticationRequest,
                                                   BindingResult result){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (Objects.nonNull(errors)) return errors;

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtil.generateToken(authenticationRequest.getEmail());

        return ResponseEntity.ok(new JWTSuccessResponse(true, token));
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest,
                                               BindingResult result){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (Objects.nonNull(errors)) return errors;
        userService.createUser(convertRequestToUser(registrationRequest));
        return ResponseEntity.ok(new MessageResponse("Successfully"));
    }

    private User convertRequestToUser(RegistrationRequest registrationRequest){
        return modelMapper.map(registrationRequest, User.class);
    }
}
