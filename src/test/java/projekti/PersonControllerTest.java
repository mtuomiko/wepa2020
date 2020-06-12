package projekti;

import projekti.model.Person;
import projekti.repository.PersonRepository;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void canAddPersonAndFindIt() throws Exception {
        mockMvc.perform(post("/register")
                .param("username", "kayttaja")
                .param("password", "secret")
                .param("name", "Keijo Käyttäjä")
                .param("slug", "keke"))
                .andExpect(status().is3xxRedirection());

        List<Person> people = personRepository.findAll();

        boolean found = false;
        for (Person person : people) {
            if (person.getUsername().equals("kayttaja")) {
                found = true;
                break;
            }
        }

        assertTrue("Did not find added user 'kayttaja' in the repository.", found);
    }

}
