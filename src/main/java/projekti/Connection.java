package projekti;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
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
    LocalDateTime requested;

    LocalDateTime approved;

}
