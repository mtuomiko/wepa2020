package projekti.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import projekti.model.Praise;
import projekti.model.Skill;
import projekti.repository.PersonRepository;
import projekti.repository.PraiseRepository;
import projekti.repository.SkillRepository;

@Controller
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private PraiseRepository praiseRepository;

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
            return "redirect:/";
        }
        model.addAttribute("person", existingPerson);

        List<Skill> skills = skillRepository.findByPersons(existingPerson);
        model.addAttribute("skills", skills);

        //model.addAttribute("connections", existingPerson.getApprovedConnections());
        return "profile";
    }

    @GetMapping("/search")
    public String searchPeople(Model model, @RequestParam(required = false) String search) {
        List<Person> people;
        if (search == null) {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("registered").ascending());
            people = personRepository.findAll(pageable).getContent();
        } else {
            people = personRepository.findByNameContainingIgnoreCase(search);
        }

        model.addAttribute("people", people);
        return "people";
    }

    @PostMapping("/people/{slug}/skill")
    public String addSkill(@PathVariable String slug, @RequestParam String name) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getName() != slug) {
            return "redirect:/";
        }
        Person existingPerson = personRepository.findBySlug(slug);
        if (existingPerson == null) {
            return "redirect:/";
        }
        if (name == null || name.isEmpty()) {
            return "redirect:/people/" + slug;
        }

        Skill existingSkill = skillRepository.findByName(name);
        if (existingSkill == null) {
            Skill newSkill = new Skill();
            newSkill.setName(name);
            newSkill.getPersons().add(existingPerson);
            skillRepository.save(newSkill);
        } else {
            existingSkill.getPersons().add(existingPerson);
            skillRepository.save(existingSkill);
        }
        return "redirect:/people/" + slug;
    }

    @PostMapping("/people/{slug}/skill/{id}/praise")
    public String toggleSkillPraise(@PathVariable String slug, @PathVariable Long id) {
        Person existingPerson = personRepository.findBySlug(slug);
        Skill existingSkill = skillRepository.getOne(id);
        if (existingPerson == null || existingSkill == null) {
            return "redirect:/";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person user = personRepository.findByUsername(auth.getName());

        Praise existingPraise = praiseRepository.findBySkillAndPraiserAndPraised(existingSkill, user, existingPerson);
        if (existingPraise == null) {
            Praise newPraise = new Praise();
            newPraise.setSkill(existingSkill);
            newPraise.setPraiser(user);
            newPraise.setPraised(existingPerson);
            praiseRepository.save(newPraise);
        }
        return "redirect:/people/" + slug;
    }

}
