package projekti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.model.Connection;
import projekti.repository.ConnectionRepository;
import projekti.repository.PersonRepository;

@Controller
public class ConnectionController {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private PersonRepository personRepository;

    @PostMapping("/connection")
    public String addConnection(@RequestParam String requesterUsername, @RequestParam String receiverUsername) {

        Connection connection = new Connection();
        connection.setRequester(personRepository.findByUsername(requesterUsername));
        connection.setReceiver(personRepository.findByUsername(receiverUsername));

        connectionRepository.save(connection);
        return "redirect:index";
    }
}
