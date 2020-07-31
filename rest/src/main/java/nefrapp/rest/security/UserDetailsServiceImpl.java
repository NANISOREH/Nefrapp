package nefrapp.rest.security;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import nefrapp.rest.model.Utente;
import nefrapp.rest.repository.UtenteRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione concreta di UserDetailsService
 * Fornisce all'AuthenticationManager di Spring Security i servizi necessari per autenticare un utente
 * ottenendone i dati con l'opportuna classe Repository.
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UtenteRepository applicationUserRepository;
    private List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<SimpleGrantedAuthority>();

    public UserDetailsServiceImpl(UtenteRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utente applicationUser = applicationUserRepository.findByCodiceFiscale(username);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(username);
        }
        grantedAuthorities = new ArrayList<SimpleGrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority(applicationUser.getAuthorities()));
        return new User(applicationUser.getCodiceFiscale(), applicationUser.getPassword(), grantedAuthorities);
    }
}