package projekti.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.model.Like;
import projekti.model.Person;
import projekti.model.Post;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Like findByPostAndSender(Post post, Person sender);

    List<Like> findBySenderAndPostIn(Person sender, Collection<Post> posts);

    long countByPost(Post post);
}
