package by.av.test.testavby.service;

import by.av.test.testavby.dto.UserDTO;
import by.av.test.testavby.entity.User;
import by.av.test.testavby.exception.UserExistsException;
import by.av.test.testavby.exception.UserNotFoundException;
import by.av.test.testavby.repository.CustomUserRepository;
import by.av.test.testavby.enums.ERole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final CustomUserRepository customUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(CustomUserRepository customUserRepository, PasswordEncoder passwordEncoder) {
        this.customUserRepository = customUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(ERole.ROLE_USER);

        Optional<User> userFoundByEmail = customUserRepository.findUserByEmail(user.getEmail());
        Optional<User> userFoundByPhoneNumber = customUserRepository.findUserByPhoneNumber(user.getPhoneNumber());

        if (userFoundByPhoneNumber.isPresent() && userFoundByEmail.isPresent()) {
            throw new UserExistsException("User with this phone number '" + user.getPhoneNumber()
                    + "' and this email '" + user.getEmail() + "' already exists!");
        } else if (userFoundByEmail.isPresent()) {
            throw new UserExistsException("User with this email '" + user.getEmail() + "' already exists!");
        } else if (userFoundByPhoneNumber.isPresent()) {
            throw new UserExistsException("User with this phone number '" + user.getPhoneNumber() + "' already exists!");
        }

        if (user.getPhoneNumber().contains("+")) {
            user.setPhoneNumber(user.getPhoneNumber().substring(1));
        }
        customUserRepository.save(user);
    }

    @Transactional
    public User updateByUserDTOAndUserId(UserDTO userDTO, Long userId) {
        User oldUser = customUserRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with this id not found"));
        oldUser.setEmail(userDTO.getEmail());
        oldUser.setFirstname(userDTO.getFirstname());
        oldUser.setLastname(userDTO.getLastname());
        oldUser.setPatronymic(userDTO.getPatronymic());
        oldUser.setPhoneNumber(userDTO.getPhoneNumber());

        return customUserRepository.save(oldUser);
    }

    @Transactional
    public User updateByUserAndPrincipal(User user, Principal principal) {
        User currentUser = convertPrincipalToUser(principal);

        currentUser.setEmail(user.getEmail());
        currentUser.setPhoneNumber(user.getPhoneNumber());
        currentUser.setFirstname(user.getFirstname());
        currentUser.setLastname(user.getLastname());
        currentUser.setPatronymic(user.getPatronymic());

        return customUserRepository.save(currentUser);
    }

    @Transactional
    public void deleteCurrentUser(Principal principal) {
        customUserRepository.delete(convertPrincipalToUser(principal));
    }

    @Transactional
    public void deleteUserById(Long userId) {
        customUserRepository.deleteById(userId);
    }

    public User getCurrentUserByPrincipal(Principal principal) {
        return convertPrincipalToUser(principal);
    }

    public Page<User> findAllSortedByNameAndSurname(int page, int size) {
        return customUserRepository.findAll(PageRequest.of(page, size, Sort.by("firstname", "lastname")));
    }

    private User convertPrincipalToUser(Principal principal) {
        return customUserRepository.findUserByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User with email " + principal.getName() + " not found"));
    }

}
