package projekti.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.model.Person;
import projekti.repository.PersonRepository;

@Controller
@ControllerAdvice
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String registration(@Valid @ModelAttribute Person person) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            return "register";
        }
        return "redirect:/";
    }

    @PostMapping("/register")
    public String newRegistration(
            @Valid @ModelAttribute Person person,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        Person existingUsername = personRepository.findByUsername(person.getUsername());
        if (existingUsername != null) {
            bindingResult.rejectValue("username", "error.person", "Username already exists");
        }

        Person existingSlug = personRepository.findBySlug(person.getSlug());
        if (existingSlug != null) {
            bindingResult.rejectValue("slug", "error.person", "Slug already exists");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personRepository.save(person);
        return "redirect:login";
    }

    @GetMapping("/people/{slug}")
    public String getProfile(Model model, @PathVariable String slug) {
        Person existingPerson = personRepository.findBySlug(slug);
        if (existingPerson == null) {
            return "redirect:/index";
        }
        model.addAttribute("person", existingPerson);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        model.addAttribute("connections", existingPerson.getApprovedConnections());

        return "profile";
    }

    @PostMapping("/search")
    public String searchPeople(Model model, @RequestParam String search) {
        List<Person> foundPeople = personRepository.findByNameContainingIgnoreCase(search);
        model.addAttribute("people", foundPeople);
        return "redirect:/connection";
    }

    /**
     * Affects all Controllers by adding currently logged in user to the model.
     */
    @ModelAttribute
    public void populateUser(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            return;
        }
        Person person = personRepository.findByUsername(auth.getName());
        model.addAttribute("user", person);
    }

}
