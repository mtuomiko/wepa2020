package projekti.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.model.Person;
import projekti.model.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    List<Skill> findByPersons(Person person);

    Skill findByName(String name);

}
