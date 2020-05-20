package projekti.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post_like")
public class Like extends AbstractPersistable<Long> {

    @ManyToOne
    private Post post;

    @ManyToOne
    private Person sender;

    @CreationTimestamp
    private LocalDateTime sendTime;
}
