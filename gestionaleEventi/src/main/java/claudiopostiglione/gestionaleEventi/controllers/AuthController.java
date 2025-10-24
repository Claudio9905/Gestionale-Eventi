package claudiopostiglione.gestionaleEventi.controllers;

import claudiopostiglione.gestionaleEventi.entities.Utente;
import claudiopostiglione.gestionaleEventi.exceptions.ValidationExcpetion;
import claudiopostiglione.gestionaleEventi.payload.LoginDTO;
import claudiopostiglione.gestionaleEventi.payload.LoginResponseDTO;
import claudiopostiglione.gestionaleEventi.payload.UtenteDTO;
import claudiopostiglione.gestionaleEventi.services.AuthService;
import claudiopostiglione.gestionaleEventi.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UtenteService utenteService;


    // 1. POST per LOGIN
    @PostMapping
    public LoginResponseDTO login (@RequestBody LoginDTO body){
        return new LoginResponseDTO(this.authService.checkAndCreateToken(body));
    }

    // 2. POST per REGISTER
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Utente register(@RequestBody @Validated UtenteDTO bodyUtente, BindingResult validationResult){
        if(validationResult.hasErrors()){
            throw new ValidationExcpetion(validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return this.utenteService.createUtente(bodyUtente);
    }


}
