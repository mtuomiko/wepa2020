package projekti.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.model.Connection;
import projekti.model.Person;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    Connection findByRequesterAndReceiver(Person requester, Person receiver);
}
