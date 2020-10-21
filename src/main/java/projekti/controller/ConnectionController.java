package projekti.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.model.Connection;
import projekti.model.Person;
import projekti.repository.ConnectionRepository;
import projekti.repository.PersonRepository;

@Controller
public class ConnectionController {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private PersonRepository personRepository;

    @PostMapping("/connection")
    public String requestConnection(@RequestParam String receiverUsername) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String requesterUsername = auth.getName();

        if (requesterUsername.equals(receiverUsername)) {
            return "redirect:/connection";
        }

        Person requester = personRepository.findByUsername(requesterUsername);
        Person receiver = personRepository.findByUsername(receiverUsername);

        if (requester == null || receiver == null) {
            return "redirect:/connection";
        }

        Connection existingRequestedConnection = connectionRepository.findByRequesterAndReceiver(requester, receiver);
        Connection existingReceivedConnection = connectionRepository.findByRequesterAndReceiver(receiver, requester);
        if (existingRequestedConnection != null || existingReceivedConnection != null) {
            return "redirect:/connection";
        }

        Connection connection = new Connection();
        connection.setRequester(requester);
        connection.setReceiver(receiver);

        connectionRepository.save(connection);
        return "redirect:/connection";
    }

    @GetMapping("/connection")
    public String searchConnections(Model model, @RequestParam(required = false) String search) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Person person = personRepository.findByUsername(username);
        List<Connection> connections = person.getConnections();
        model.addAttribute("connections", connections);

        if (search != null) {
            List<Person> foundPeople = personRepository.findByNameContainingIgnoreCase(search);
            List<Person> unwantedResults = new ArrayList<>();
            unwantedResults.add(person);
            for (Connection connection : connections) {
                unwantedResults.add(connection.getOtherParty());
            }
            foundPeople.removeIf(p -> unwantedResults.contains(p));
            model.addAttribute("people", foundPeople);
        }

        return "connections";
    }

    @PostMapping("/connection/{id}")
    public String approveConnection(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Connection connection = connectionRepository.getOne(id);

        if (connection == null) {
            return "redirect:/connection";
        }

        if (connection.getApproved() != null) {
            return "redirect:/connection";
        }

        if (!username.equals(connection.getReceiver().getUsername())) {
            return "redirect:/connection";
        }

        connection.setApproved(LocalDateTime.now());
        connectionRepository.save(connection);
        return "redirect:/connection";
    }

    @PostMapping("/connection/{id}/remove")
    public String deleteConnection(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Connection connection = connectionRepository.getOne(id);

        if (connection == null) {
            return "redirect:/connection";
        }

        String requesterUsername = connection.getRequester().getUsername();
        String receiverUsername = connection.getReceiver().getUsername();

        if (username.equals(requesterUsername) || username.equals(receiverUsername)) {
            connectionRepository.delete(connection);
        }

        return "redirect:/connection";
    }

}
