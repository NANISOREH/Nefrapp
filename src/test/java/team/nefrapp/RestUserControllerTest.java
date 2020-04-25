package team.nefrapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.WebApplicationContext;
import team.nefrapp.model.Paziente;
import team.nefrapp.model.Utente;
import team.nefrapp.repository.PazienteRepository;
import team.nefrapp.repository.UtenteRepository;
import team.nefrapp.rest.RestUserController;

import javax.naming.AuthenticationException;
import javax.servlet.http.Cookie;
import java.util.Map;
import java.util.logging.Logger;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestUserControllerTest {
    Logger log = Logger.getLogger("Test");
    @LocalServerPort
    int port = 8080;
    private String token = null;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UtenteRepository repo;
    @Autowired
    private PazienteRepository pazRepo;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private RestUserController controller;

    private Paziente p = null;

    @Test
    public void testRegistrazione(){
        repo.deleteAll();

        p = new Paziente();
        p.setAttivo(true);
        p.setCodiceFiscale("MSNDNC90M32B461O");
        p.setPassword("662de9b86e5898d68821ae896d29cd765fd7d3b3020bc55057dcb8fd1e0ddb0da51f4e47d81a7c4c605da1286dc7b49d5d2e622525bd2819d72cc730dafa5e02");
        p.setAuthorities("ROLE_PAZIENTE");

        HttpStatus status = restTemplate.postForObject("http://localhost:8080/sign-up", p, HttpStatus.class);

        Utente retrieved = repo.findByCodiceFiscale(p.getCodiceFiscale());
        assert(retrieved.getCodiceFiscale().equals(p.getCodiceFiscale()));
        assert(retrieved.getAuthorities().equals(p.getAuthorities()));
        assert(status == HttpStatus.CREATED);
    }

    @Test
    public void testLogin() throws AuthenticationException, JsonProcessingException, javax.security.sasl.AuthenticationException {
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("username", "MSNDNC90M32B461O");
        map.add("password", "662de9b86e5898d68821ae896d29cd765fd7d3b3020bc55057dcb8fd1e0ddb0da51f4e47d81a7c4c605da1286dc7b49d5d2e622525bd2819d72cc730dafa5e02");
        Map<String, String> response = restTemplate.postForObject("http://localhost:8080/auth", map, Map.class);
        assertThat(response.get("token")).isNotNull();
        assertThat(response.get("role").equals("ROLE_PAZIENTE"));
    }

    @Test
    public void testEditPaz() throws Exception {
        p = new Paziente();
        p.setAttivo(true);
        p.setCodiceFiscale("MSNDNC90M32B461O");
        p.setNome("Nico");
        p.setPassword("");
        p.setAuthorities("ROLE_PAZIENTE");

        //testo iniettando direttamente il controller perchè non ora come ora non saprei
        //come includere nella request tramite resttemplate il cookie con il token per l'autenticazione
        ResponseEntity result = controller.editPaziente(p);
        Utente retrieved = pazRepo.findByCodiceFiscale(p.getCodiceFiscale());
        if (retrieved.getNome()!=null)
            assert(retrieved.getNome().equals("Nico"));
        else
            assert(false);
        assert(result.getStatusCode() == HttpStatus.OK);
        assert(result.getBody().toString().equals(p.toString()));

        repo.deleteAll();
    }
}
