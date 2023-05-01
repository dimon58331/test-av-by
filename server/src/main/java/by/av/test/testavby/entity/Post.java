package by.av.test.testavby.entity;

import by.av.test.testavby.entity.transport.Transport;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Post")
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "favorite_post"
            , joinColumns = @JoinColumn(name = "post_id")
            , inverseJoinColumns = @JoinColumn(name = "user_id")
            , foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT)
            , inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT)
    )
    private Set<User> favoriteUsers = new HashSet<>();

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "transport_id", referencedColumnName = "id")
    private Transport transport;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "title")
    private String title;

    @Column(name = "caption")
    private String caption;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(updatable = false, name = "created_date")
    private LocalDateTime createdDate;

    @OneToOne(mappedBy = "post")
    private ImageModel imageModel;

    @PrePersist
    protected void onCreate(){
        this.createdDate = LocalDateTime.now();
    }
}
