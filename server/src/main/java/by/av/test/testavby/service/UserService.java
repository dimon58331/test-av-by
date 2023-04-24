package by.av.test.testavby.service;

import by.av.test.testavby.entity.User;
import by.av.test.testavby.exception.IncorrectUserException;
import by.av.test.testavby.exception.UserExistsException;
import by.av.test.testavby.exception.UserNotFoundException;
import by.av.test.testavby.repository.UserRepository;
import by.av.test.testavby.util.ERole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.CredentialNotFoundException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(ERole.ROLE_USER);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new UserExistsException("User with this email" + user.getEmail() + "already exists!");
        }
    }

    @Transactional
    public User updateByUserAndPrincipal(User user, Principal principal){
        User currentUser = convertPrincipalToUser(principal);
        if (!Objects.equals(currentUser.getId(), user.getId())){
            throw new IncorrectUserException("You dont have permission to edit other users");
        }
        return userRepository.save(user);
    }

    private User convertPrincipalToUser(Principal principal) {
        return userRepository.findUserByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User with email " + principal.getName() + " not found"));
    }

}
