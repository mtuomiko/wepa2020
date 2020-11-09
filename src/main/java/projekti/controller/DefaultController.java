package projekti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import projekti.model.Person;
import projekti.repository.PersonRepository;

@Controller
public class DefaultController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/")
    public String helloWorld(Model model) {
        return "redirect:/search";
    }

    @GetMapping("/login")
    public String loginForm() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            return "login";
        }

        return "redirect:/search";
    }
}
