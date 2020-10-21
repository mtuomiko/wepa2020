package projekti.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Praise extends AbstractPersistable<Long> {

    @ManyToOne
    private Skill skill;

    @ManyToOne
    private Person praiser;

    @ManyToOne
    private Person praised;

    @CreationTimestamp
    private LocalDateTime sendTime;
}
