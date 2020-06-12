package projekti.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person extends AbstractPersistable<Long> {

    @OneToMany(mappedBy = "requester")
    private List<Connection> requestedConnections = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    private List<Connection> receivedConnections = new ArrayList<>();

    @OneToMany(mappedBy = "sender")
    private List<Post> sentPosts = new ArrayList<>();

    @OneToMany(mappedBy = "sender")
    private List<Comment> sentComments = new ArrayList<>();

    @OneToMany(mappedBy = "person")
    private List<Skill> skills = new ArrayList<>();

    @OneToMany(mappedBy = "person")
    private List<Praise> praises = new ArrayList<>();

    @OneToOne
    private ImageFile imageFile;

    @Column(unique = true)
    @Size(min = 3, max = 50)
    private String username;

    private String password;

    @Size(min = 3, max = 255)
    private String name;

    @Column(unique = true)
    @Size(min = 3, max = 20)
    private String slug;

    @CreationTimestamp
    private LocalDateTime registered;

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.username);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return true;
    }

}
