package projekti.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import projekti.model.Person;
import projekti.repository.PersonRepository;

@Controller
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/people")
    public String register(@Valid @ModelAttribute Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personRepository.save(person);
        return "redirect:index";
    }
}
