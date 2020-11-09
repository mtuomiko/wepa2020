package projekti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projekti.model.Person;
import projekti.model.Praise;
import projekti.model.Skill;

public interface PraiseRepository extends JpaRepository<Praise, Long> {

    Praise findBySkillAndPraiserAndPraised(Skill skill, Person praiser, Person Praised);

    Long countBySkillAndPraised(Skill skill, Person praised);
}
