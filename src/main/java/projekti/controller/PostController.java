package projekti.controller;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import projekti.model.Person;
import projekti.model.Post;
import projekti.repository.PersonRepository;
import projekti.repository.PostRepository;

@Controller
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/posts")
    public String getPostStream(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person user = personRepository.findByUsername(auth.getName());
        List<Person> contacts = user.getApprovedContacts();
        contacts.add(user);

        Pageable pageable = PageRequest.of(0, 25, Sort.by("sendTime").descending());
        List<Post> posts = postRepository.findBySenderIn(contacts, pageable);

        model.addAttribute("posts", posts);
        return "posts";
    }

    @PostMapping("/posts")
    public String createNewPost(@Valid @ModelAttribute Post post, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "posts";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person user = personRepository.findByUsername(auth.getName());

        post.setSender(user);
        postRepository.save(post);

        return "redirect:/posts";
    }
}
