package claudiopostiglione.gestionaleEventi.controllers;

import claudiopostiglione.gestionaleEventi.entities.Prenotazione;
import claudiopostiglione.gestionaleEventi.entities.Utente;
import claudiopostiglione.gestionaleEventi.exceptions.ValidationExcpetion;
import claudiopostiglione.gestionaleEventi.payload.PrenotazioneDTO;
import claudiopostiglione.gestionaleEventi.services.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// URL: http://localhost:3001/....
@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    //Endpoint "/me"
    @GetMapping("/me")
    @PreAuthorize(("hasAuthority('UTENTE_NORMALE')"))
    public Page<Prenotazione> getAllMyPrenotazioni(@AuthenticationPrincipal Utente currentUtente, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy){
        return this.prenotazioneService.findAllPrenotazioni(page,size,sortBy);
    }

    //Endpoint "/prenotazioni/..."
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(("hasAuthority('UTENTE_NORMALE')"))
    public Prenotazione createPrenotazione(@RequestBody @Validated PrenotazioneDTO bodyPrenotazione, BindingResult validationResult){
        if(validationResult.hasErrors()){
            throw new ValidationExcpetion(validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }

        return this.prenotazioneService.createPrenotazione(bodyPrenotazione);
    }

    @DeleteMapping("/me/{prenotazioneId}")
    @PreAuthorize(("hasAuthority('UTENTE_NORMALE')"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getPrenotazioneAndDelete(@AuthenticationPrincipal Utente currentUtente,@PathVariable UUID prenotazioneId){
        this.prenotazioneService.findPrenotazioneByIdAndDelete(prenotazioneId, currentUtente);
    }


}
