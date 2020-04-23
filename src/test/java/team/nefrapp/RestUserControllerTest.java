package team.nefrapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import team.nefrapp.model.Paziente;
import team.nefrapp.model.Utente;
import team.nefrapp.repository.PazienteRepository;
import team.nefrapp.repository.UtenteRepository;
import team.nefrapp.rest.RestUserController;

import javax.naming.AuthenticationException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestUserControllerTest {
    @LocalServerPort
    int port = 8080;
    private String token = null;
    @Autowired
    private UtenteRepository repo;
    @Autowired
    private TestRestTemplate restTemplate;

    private Paziente p = null;

    @Test
    public void testRegistrazione(){
        repo.deleteAll();

        p = new Paziente();
        p.setIsAttivo(true);
        p.setCodiceFiscale("MSNDNC90M32B461O");
        p.setPassword("662de9b86e5898d68821ae896d29cd765fd7d3b3020bc55057dcb8fd1e0ddb0da51f4e47d81a7c4c605da1286dc7b49d5d2e622525bd2819d72cc730dafa5e02");
        p.setAuthorities("ROLE_MEDICO");

        restTemplate.postForObject("http://localhost:8080/sign-up", p, void.class);

        Utente retrieved = repo.findByCodiceFiscale(p.getCodiceFiscale());
        assert(retrieved.getCodiceFiscale().equals(p.getCodiceFiscale()));
        assert(retrieved.getAuthorities().equals(p.getAuthorities()));
    }

    @Test
    public void testLogin() throws AuthenticationException, JsonProcessingException, javax.security.sasl.AuthenticationException {
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("username", "MSNDNC90M32B461O");
        map.add("password", "662de9b86e5898d68821ae896d29cd765fd7d3b3020bc55057dcb8fd1e0ddb0da51f4e47d81a7c4c605da1286dc7b49d5d2e622525bd2819d72cc730dafa5e02");
        token = restTemplate.postForObject("http://localhost:8080/auth", map, String.class);
        assertThat(token).isNotNull();

        repo.deleteAll();
    }
}
