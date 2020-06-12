package projekti.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import projekti.model.Person;
import projekti.repository.PersonRepository;

@Controller
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
        return "redirect:/index";
    }

    @PostMapping("/register")
    public String newRegistration(
            @Valid @ModelAttribute Person person,
            BindingResult bindingResult) {

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
        return "profile";
    }

}
