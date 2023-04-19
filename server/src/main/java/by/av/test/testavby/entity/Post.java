package by.av.test.testavby.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "Post")
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /*
     * TODO
     *  Check Cascade Type
     * */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    /*
     * TODO
     *  Check Cascade Type
     * */
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "transport_id", referencedColumnName = "id")
    private Transport transport;
    @Column(name = "price", nullable = false)
    @NotNull(message = "Price cannot be null")
    @Min(value = 0, message = "Price cannot be less than 0")
    private Double price;
    @Column(name = "title")
    private String title;
    @Column(name = "caption")
    private String caption;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(updatable = false, name = "created_date")
    private LocalDateTime createdDate;
    /*
     * TODO
     *  Check Cascade Type
     * */
    @OneToOne(mappedBy = "post")
    private ImageModel imageModel;
    @PrePersist
    protected void onCreate(){
        this.createdDate = LocalDateTime.now();
    }
}
