package claudiopostiglione.gestionaleEventi.controllers;

import claudiopostiglione.gestionaleEventi.entities.Prenotazione;
import claudiopostiglione.gestionaleEventi.exceptions.ValidationExcpetion;
import claudiopostiglione.gestionaleEventi.payload.PrenotazioneDTO;
import claudiopostiglione.gestionaleEventi.services.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @DeleteMapping("/{prenotazioneId}")
    @PreAuthorize(("hasAuthority('UTENTE_NORMALE')"))
    public void getPrenotazioneAndDelete(@PathVariable UUID prenotazioneId){
        this.prenotazioneService.findPrenotazioneByIdAndDelete(prenotazioneId);
    }


}
