/*
package nefrapp.site.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
*/
/*                .antMatchers(HttpMethod.POST, "/login", "/site").permitAll()
                .antMatchers(HttpMethod.GET, "/dashboard").authenticated()
                .antMatchers(HttpMethod.GET, "/login", "/team", "/", "/error", "/utenti", "/provapaz", "/provamed",
                        "/js/**", "/includes/**", "/css/**", "/img/**", "/vendor/**", "/nuget/**", "/scss/**", "/clean",
                        "/themes/**", "/deleteUser").permitAll()
                .antMatchers(HttpMethod.DELETE, "/deleteUser").permitAll()

                //PERMESSI PAGINE DI SERVIZIO
                .antMatchers(HttpMethod.POST, "/utenti").permitAll()
                .antMatchers(HttpMethod.GET, "/clean").authenticated()*//*

                .anyRequest().permitAll()


                .and()
                .exceptionHandling().accessDeniedPage("/login")
*/
/*                .and()
                //.addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)*//*
;
        ;

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://localhost:8080/", "https://localhost:8081/"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }
}*/
