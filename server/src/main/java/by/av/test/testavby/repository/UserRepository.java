package by.av.test.testavby.repository;

import by.av.test.testavby.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserRepository extends JpaRepository<User, Long> {
    /*
    * TODO
    *  Create index for email in DB
    * */
    public Optional<User> findUserByEmail(String email);
    /*
     * TODO
     *  Create index for phone number in DB
     * */
    public Optional<User> findUserByPhoneNumber(String phoneNumber);
}
