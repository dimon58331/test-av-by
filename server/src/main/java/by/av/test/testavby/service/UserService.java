package by.av.test.testavby.service;

import by.av.test.testavby.entity.User;
import by.av.test.testavby.exception.UserExistsException;
import by.av.test.testavby.exception.UserNotFoundException;
import by.av.test.testavby.repository.UserRepository;
import by.av.test.testavby.enums.ERole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

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

        currentUser.setEmail(user.getEmail());
        currentUser.setPhoneNumber(user.getPhoneNumber());
        currentUser.setFirstname(user.getFirstname());
        currentUser.setLastname(user.getLastname());
        currentUser.setPatronymic(user.getPatronymic());

        return userRepository.save(currentUser);
    }

    public User getCurrentUserByPrincipal(Principal principal){
        return convertPrincipalToUser(principal);
    }

    private User convertPrincipalToUser(Principal principal) {
        return userRepository.findUserByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User with email " + principal.getName() + " not found"));
    }

}
