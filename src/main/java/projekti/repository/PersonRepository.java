package projekti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projekti.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findByUsername(String username);
    
    Person findBySlug(String slug);
}
