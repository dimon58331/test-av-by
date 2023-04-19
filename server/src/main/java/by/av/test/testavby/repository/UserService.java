package by.av.test.testavby.repository;

import by.av.test.testavby.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService extends JpaRepository<User, Long> {
    /*
    * TODO
    *  Create index for email
    * */
    public Optional<User> findUserByEmail(String email);
    /*
     * TODO
     *  Create index for phone number
     * */
    public Optional<User> findUserByPhoneNumber(String phoneNumber);
}
