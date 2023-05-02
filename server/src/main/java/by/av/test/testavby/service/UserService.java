package by.av.test.testavby.service;

import by.av.test.testavby.entity.User;
import by.av.test.testavby.exception.UserExistsException;
import by.av.test.testavby.exception.UserNotFoundException;
import by.av.test.testavby.repository.UserRepository;
import by.av.test.testavby.enums.ERole;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(ERole.ROLE_USER);

        Optional<User> userFoundByEmail = userRepository.findUserByEmail(user.getEmail());
        Optional<User> userFoundByPhoneNumber = userRepository.findUserByPhoneNumber(user.getPhoneNumber());

        if (userFoundByPhoneNumber.isPresent() && userFoundByEmail.isPresent()){
            throw new UserExistsException("User with this phone number '" + user.getPhoneNumber()
                    + "' and this email '" + user.getEmail() + "' already exists!");
        } else if (userFoundByEmail.isPresent()) {
            throw new UserExistsException("User with this email '" + user.getEmail() + "' already exists!");
        } else if (userFoundByPhoneNumber.isPresent()){
            throw new UserExistsException("User with this phone number '" + user.getPhoneNumber() + "' already exists!");
        }
        userRepository.save(user);
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

    @Transactional
    public void deleteCurrentUser(Principal principal){
        userRepository.delete(convertPrincipalToUser(principal));
    }

    public User getCurrentUserByPrincipal(Principal principal){
        return convertPrincipalToUser(principal);
    }

    public Page<User> findAllSortedByNameAndSurname(int page, int size){
        return userRepository.findAll(PageRequest.of(page, size, Sort.by("firstname", "lastname")));
    }

    private User convertPrincipalToUser(Principal principal) {
        return userRepository.findUserByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User with email " + principal.getName() + " not found"));
    }

}
