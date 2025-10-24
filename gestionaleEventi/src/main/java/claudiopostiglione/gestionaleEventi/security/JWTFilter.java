package claudiopostiglione.gestionaleEventi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTTools jwtTools;

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


        filterChain.doFilter(request,response);

    }

    // Disattivazione dei filtri per determinati endpoint
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
