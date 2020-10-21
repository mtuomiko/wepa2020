package projekti.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Connection extends AbstractPersistable<Long> {

    @OneToMany(mappedBy = "connection")
    private List<Post> posts = new ArrayList<>();

    @ManyToOne
    private Person requester;

    @ManyToOne
    private Person receiver;

    @CreationTimestamp
    private LocalDateTime requested;

    private LocalDateTime approved;

    public Person getOtherParty() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if (username.equals(requester.getUsername())) {
            return receiver;
        }
        return requester;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.requester);
        hash = 29 * hash + Objects.hashCode(this.receiver);
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
        final Connection other = (Connection) obj;
        if (!Objects.equals(this.requester, other.requester)) {
            return false;
        }
        if (!Objects.equals(this.receiver, other.receiver)) {
            return false;
        }
        return true;
    }

}
