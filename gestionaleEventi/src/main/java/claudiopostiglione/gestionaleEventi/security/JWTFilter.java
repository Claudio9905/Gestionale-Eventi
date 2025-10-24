package claudiopostiglione.gestionaleEventi.security;

import claudiopostiglione.gestionaleEventi.entities.Utente;
import claudiopostiglione.gestionaleEventi.services.UtenteService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private UtenteService utenteService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //----------------------------- AUTENTICAZIONE --------------------------------
        // 1.Verifica della presenza del HEADER AUTHORIZATION
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new UnavailableException("Inserire il token nell'authorization header e nel formato giusto");
        }

        // 2.Verifica della validità del TOKEN
        String accessToken = authHeader.replace("Bearer ","");
        jwtTools.verifyToken(accessToken);

        //---------------------------- AUTORIZZAZIONE ---------------------------------

        // 1.Ricerca dell'utente nel DB
        UUID idUtente = jwtTools.extractIdFromToken(accessToken);
        Utente utenteFound = this.utenteService.findUtenteById(idUtente);

        //Associazione dell'utente al Security Context
        Authentication authentication = new UsernamePasswordAuthenticationToken(utenteFound, null, utenteFound.getAuthorities());
        //Aggiornamento del Security COntext associando ad esso l'utente corrente e il suo ruolo
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request,response);

    }

    // Disattivazione dei filtri per determinati endpoint
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
