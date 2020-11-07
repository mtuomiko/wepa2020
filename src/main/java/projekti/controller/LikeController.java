package projekti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import projekti.model.Like;
import projekti.model.Person;
import projekti.model.Post;
import projekti.repository.LikeRepository;
import projekti.repository.PersonRepository;
import projekti.repository.PostRepository;

@Controller
public class LikeController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PersonRepository personRepository;

    @PostMapping("/posts/{id}/like")
    @ResponseBody
    public String togglePostLike(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person user = personRepository.findByUsername(auth.getName());

        Post post = postRepository.getOne(id);

        Like like = likeRepository.findByPostAndSender(post, user);
        if (like == null) {
            Like newLike = new Like();
            newLike.setPost(post);
            newLike.setSender(user);
            likeRepository.save(newLike);
        } else {
            likeRepository.delete(like);
        }

        return "";
    }
}
