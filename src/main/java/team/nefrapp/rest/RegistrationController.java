package team.nefrapp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team.nefrapp.entity.Utente;
import team.nefrapp.repository.UtenteRepository;

@RestController
@RequestMapping("/users")
public class RegistrationController {
    private UtenteRepository applicationUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public RegistrationController(UtenteRepository applicationUserRepository,
                                  BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Utente user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        applicationUserRepository.save(user);
    }
}