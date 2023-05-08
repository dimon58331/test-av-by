package by.av.test.testavby.entity;

import by.av.test.testavby.entity.transport.TransportParameters;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ElementCollection(targetClass = String.class)
    private Set<String> likedUsers = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "transport_id")
    private TransportParameters transportParameters;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "title")
    private String title;

    @Column(name = "caption")
    private String caption;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(updatable = false, name = "created_date")
    private LocalDateTime createdDate;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private ImageModel imageModel;

    @PrePersist
    protected void onCreate(){
        this.createdDate = LocalDateTime.now();
    }
}
