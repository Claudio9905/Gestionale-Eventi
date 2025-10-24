package claudiopostiglione.gestionaleEventi.security;

import claudiopostiglione.gestionaleEventi.entities.Utente;
import claudiopostiglione.gestionaleEventi.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JWTTools {

    @Value("${jwt.secret}")
    private String keySecret;

    //Creazione del token
    public String createToken(Utente utente) {

        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .subject(String.valueOf(utente))
                .signWith(Keys.hmacShaKeyFor(keySecret.getBytes()))
                .compact();

    }

    //validità del token
    public void verifyToken(String accessToken) {
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(keySecret.getBytes())).build().parse(accessToken);
        } catch (Exception ex) {
            throw new UnauthorizedException("Ci sono stati errori nel token, effettua il login");
        }
    }

    //Estrazione dell'id dal token
    public UUID extractIdFromToken(String accessToken){
        return UUID.fromString(Jwts.parser().verifyWith(Keys.hmacShaKeyFor(keySecret.getBytes())).build().parseSignedClaims(accessToken).getPayload().getSubject());
    }

}
