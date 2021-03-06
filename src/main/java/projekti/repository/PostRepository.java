package projekti.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.model.Post;
import projekti.model.Person;

public interface PostRepository extends JpaRepository<Post, Long> {

    //@EntityGraph(value = "Post.includeCommentsAndLikes")
    List<Post> findBySenderIn(Collection<Person> senders, Pageable pageable);

}
