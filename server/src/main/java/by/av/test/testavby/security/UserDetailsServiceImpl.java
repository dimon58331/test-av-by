package by.av.test.testavby.security;

import by.av.test.testavby.entity.User;
import by.av.test.testavby.repository.CustomUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final CustomUserRepository customUserRepository;
    private final Logger LOG = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    public UserDetailsServiceImpl(CustomUserRepository customUserRepository) {
        this.customUserRepository = customUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = customUserRepository.findUserByEmail(email);
        if (userOptional.isEmpty()){
            LOG.info("User with this email not found!");
            throw new UsernameNotFoundException("User with this email not found!");
        }
        return new UserDetailsImpl(userOptional.get());
    }
}
