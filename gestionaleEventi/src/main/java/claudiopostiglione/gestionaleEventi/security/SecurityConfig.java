package claudiopostiglione.gestionaleEventi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        //Disabilitazione dell'autenticazione basata sul form di default di Spring Security
        httpSecurity.formLogin(formLogin-> formLogin.disable());
        // Disattivazione della protezione CSRF
        httpSecurity.csrf(csrf->csrf.disable());
        //Disabilitazione delle sessioni
        httpSecurity.sessionManagement(sessions-> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Funzionalità aggiuntive
        httpSecurity.cors(Customizer.withDefaults());

        return httpSecurity.build();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3001"));
        // Creo una lista di uno o più indirizzi FRONTEND che voglio possano accedere a questo BACK-END senza problemi di CORS
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // applico la configurazione di sopra a tutti gli endpoint ("/**")
        return source;
    }

    // Utilizzo bCrypt per la sicurezza delle password degli utenti
    @Bean
    public PasswordEncoder getBcrypt(){
        return new BCryptPasswordEncoder(12);
    }


}
