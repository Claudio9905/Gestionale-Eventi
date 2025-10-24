package claudiopostiglione.gestionaleEventi.services;

import claudiopostiglione.gestionaleEventi.entities.Utente;
import claudiopostiglione.gestionaleEventi.exceptions.UnauthorizedException;
import claudiopostiglione.gestionaleEventi.payload.LoginDTO;
import claudiopostiglione.gestionaleEventi.security.JWTTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private UtenteService utenteService;
    @Autowired
    private PasswordEncoder bCrypt;
    @Autowired
    private JWTTools jwtTools;

    public String checkAndCreateToken(LoginDTO bodyLogin){
        //Controllo delle credenziali d'accesso
        Utente utenteFound = this.utenteService.findUtenteByEmail(bodyLogin.email());

        if(bCrypt.matches(bodyLogin.password(), utenteFound.getPassword())){
            return jwtTools.createToken(utenteFound);
        } else {
            throw new UnauthorizedException("Credenziali non valide");
        }
    }

}
