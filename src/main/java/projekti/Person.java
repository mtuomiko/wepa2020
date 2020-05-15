package projekti;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
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

    @Column(unique = true)
    private String username;

    private String password;

    private String name;

    @Column(unique = true)
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
