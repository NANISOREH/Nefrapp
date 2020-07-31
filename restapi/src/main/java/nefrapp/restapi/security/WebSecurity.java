package nefrapp.restapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserDetailsServiceImpl userDetailsService;

    public WebSecurity(UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                //PERMESSI BACKEND
                .antMatchers(HttpMethod.POST, "/sign-paz", "/sign-med", "/sign-adm",
                        "/auth", "/edit-med", "/edit-paz").permitAll()
                .antMatchers(HttpMethod.GET, "/getuser/**").permitAll()

//              I veri permessi del backend per ora sono commentati ma è opportuno aggiornarli comunque
//                .antMatchers(HttpMethod.POST, "/edit-med").hasAnyRole("ROLE_MEDICO", "ROLE_ADMIN")
//                .antMatchers(HttpMethod.POST, "/edit-paz").hasAnyRole("ROLE_PAZIENTE", "ROLE_ADMIN")
//                .antMatchers(HttpMethod.GET, "/getuser").authenticated()

                //PERMESSI SITO
                .antMatchers(HttpMethod.POST, "/login, /site").permitAll()
                .antMatchers(HttpMethod.GET, "/login", "/team", "/dashboard", "/", "/error", "/utenti", "/provapaz", "/provamed",
                        "/js/**", "/includes/**", "/css/**", "/img/**", "/vendor/**", "/nuget/**", "/scss/**",
                        "/themes/**").permitAll()

                //PERMESSI PAGINE DI SERVIZIO
                .antMatchers(HttpMethod.POST, "/utenti").permitAll()
                .antMatchers(HttpMethod.GET, "/clean").permitAll()
                .anyRequest().authenticated()


                .and()
                .exceptionHandling().accessDeniedPage("/login")
                .and()
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        ;

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://localhost:8080/"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }
}