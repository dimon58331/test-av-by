package by.av.test.testavby.entity;

import by.av.test.testavby.util.ERole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Person")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "first_name", nullable = false)
    @NotEmpty(message = "Firstname cannot be empty")
    private String firstName;
    @Column(name = "last_name", nullable = false)
    @NotEmpty(message = "Lastname cannot be empty")
    private String lastName;
    @Column(name = "patronymic")
    private String patronymic;
    @Column(name = "password", nullable = false)
    @NotEmpty(message = "Password cannot be empty")
    private String password;
    @Column(name = "email", unique = true, nullable = false)
    @NotEmpty(message = "Email cannot be empty")
    @Email(regexp = "^\\S+@\\S+\\.\\S+$", message = "Email should be like 'test@test.test'")
    private String email;
    @Column(name = "phone_number", unique = true, nullable = false)
    @NotEmpty(message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\+?[1-9][0-9]{12,13}$",
            message = "Phone number should be like '(+)375006388351, length 12-13 digits'")
    private String phoneNumber;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE},
            fetch = FetchType.LAZY, mappedBy = "user")
    private List<Transport> transports;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE})
    @JoinTable(
            name = "favorite"
            , joinColumns = @JoinColumn(name = "user_id")
            , inverseJoinColumns = @JoinColumn(name = "transport_id")
            , uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "transport_id"})}
    )
    private Set<Transport> favoriteTransport = new HashSet<>();
    @Column(name = "role")
    @ElementCollection(targetClass = ERole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role"}))
    private Set<ERole> roles = new HashSet<>();
}
