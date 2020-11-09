package projekti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import projekti.model.Person;
import projekti.repository.PersonRepository;

@ControllerAdvice
public class GlobalController {

    @Autowired
    private PersonRepository personRepository;

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
