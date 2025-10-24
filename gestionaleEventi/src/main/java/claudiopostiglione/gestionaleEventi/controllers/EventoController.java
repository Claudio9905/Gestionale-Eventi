package claudiopostiglione.gestionaleEventi.controllers;

import claudiopostiglione.gestionaleEventi.entities.Evento;
import claudiopostiglione.gestionaleEventi.exceptions.ValidationExcpetion;
import claudiopostiglione.gestionaleEventi.payload.EventoDTO;
import claudiopostiglione.gestionaleEventi.services.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// URL: http://localhost:3001/....
@RestController
@RequestMapping("/eventi")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    //Endpoint "/eventi/..."
    @GetMapping
    @PreAuthorize(("hasAnyAuthority('ADMIN','ORGANIZZATORE_DI_EVENTI')"))
    public Page<Evento> getAllEventi(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
        return this.eventoService.findAllEventi(page, size, sortBy);
    }

    @GetMapping("/{eventoId}")
    @PreAuthorize(("hasAnyAuthority('ADMIN','ORGANIZZATORE_DI_EVENTI')"))
    @ResponseStatus(HttpStatus.FOUND)
    public Evento getEventoById(@PathVariable UUID eventoId) {
        return this.eventoService.findEventoById(eventoId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(("hasAuthority('ORGANIZZATORE_DI_EVENTI')"))
    public Evento createEvento(@RequestBody @Validated EventoDTO bodyEvento, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationExcpetion(validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return this.eventoService.saveEvento(bodyEvento);
    }

    @PutMapping("/{eventoId}")
    @PreAuthorize(("hasAuthority('ORGANIZZATORE_DI_EVENTI')"))
    public Evento getEventoByIdAndUpdate(@PathVariable UUID eventoId, @RequestBody EventoDTO bodyEvento) {
        return this.eventoService.findEventoByIdAndUpdate(eventoId, bodyEvento);
    }

    @DeleteMapping("/{eventoId}")
    @PreAuthorize(("hasAuthority('ORGANIZZATORE_DI_EVENTI')"))
    public void getEventoByIdAndDelete(@PathVariable UUID eventoId){
        this.eventoService.findEventoByIdAndDelete(eventoId);
    }

}
