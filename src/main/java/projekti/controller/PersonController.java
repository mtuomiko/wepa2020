package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @PostMapping("/people")
    public String register(@ModelAttribute Person person) {
        personRepository.save(person);
        return "redirect:index";
    }
}
