package projekti.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Skill extends AbstractPersistable<Long> {

    @OneToMany(mappedBy = "skill")
    private List<Praise> praises = new ArrayList<>();

    @ManyToMany
    private List<Person> persons = new ArrayList<>();

    @Column(unique = true)
    @NotEmpty(message = "Please enter skill name")
    private String name;
}
