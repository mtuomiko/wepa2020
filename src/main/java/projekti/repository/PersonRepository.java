package projekti.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findByUsername(String username);

    Person findBySlug(String slug);

    List<Person> findByNameContainingIgnoreCase(String name);
}
