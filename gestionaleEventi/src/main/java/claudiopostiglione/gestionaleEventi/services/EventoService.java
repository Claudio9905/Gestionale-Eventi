package claudiopostiglione.gestionaleEventi.services;

import claudiopostiglione.gestionaleEventi.entities.Evento;
import claudiopostiglione.gestionaleEventi.entities.RuoloUtente;
import claudiopostiglione.gestionaleEventi.entities.Utente;
import claudiopostiglione.gestionaleEventi.exceptions.BadRequestException;
import claudiopostiglione.gestionaleEventi.exceptions.IdNotFoundException;
import claudiopostiglione.gestionaleEventi.payload.EventoDTO;
import claudiopostiglione.gestionaleEventi.repositories.EventoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;
    @Autowired
    private UtenteService utenteService;


    //OPERAZIONI CRUD per gli eventi

    // 1. per la chiamata POST per la creazione di un evento
    public Evento saveEvento(EventoDTO body){

        Utente utenteFound = this.utenteService.findUtenteById(body.utenteId());

        if(!utenteFound.getRole().equals(RuoloUtente.ORGANIZZATORE_DI_EVENTI)) throw new BadRequestException("L'utente " + utenteFound.getNome() + " " + utenteFound.getCognome() + " con ID: " + utenteFound.getId() + " non è un organizzatore di eventi");

        Evento newEvento = new Evento(body.titolo(), body.descrizione(), body.dataEvento(), body.luogo(), body.numPostDisp());

        this.eventoRepository.save(newEvento);
        log.info("L'evento " + newEvento.getTitolo() + " con ID: " + newEvento.getId() + " è stato salvato correttamente");
        return newEvento;
    }

    // 2. per la chiamata GET che restituisce la lista degli eventi
    public Page<Evento> findAllEventi(int numPage, int sizePage, String sortBy) {
        if (sizePage > 30) sizePage = 30;
        sortBy = "titolo";
        Pageable pageable = PageRequest.of(numPage, sizePage, Sort.by(sortBy).ascending());
        return this.eventoRepository.findAll(pageable);
    }

    // 3. per la chiamata GET per un singolo evento
    public Evento findEventoById(UUID eventoId) {
        return this.eventoRepository.findById(eventoId).orElseThrow(() -> new IdNotFoundException("L'evento con ID: " + eventoId + " non è stato trovato"));
    }

    // 4. per la chiamata PUT per la modifica dell'evento
    public Evento findEventoByIdAndUpdate(UUID eventoId, EventoDTO body) {

        Evento eventoFound = this.findEventoById(eventoId);

        eventoFound.setTitolo(body.titolo());
        eventoFound.setDescrizione(body.descrizione());
        eventoFound.setDataEvento(body.dataEvento());
        eventoFound.setLuogo(body.luogo());
        eventoFound.setNumPostDisp(body.numPostDisp());


        Evento updateEvento = this.eventoRepository.save(eventoFound);

        log.info("L'evento " + updateEvento.getTitolo() + " con ID: " + updateEvento.getId() + " è stato aggiornato correttamente");

        return updateEvento;
    }

    // 5. per la chiamata DELETE di un'evento
    public void findEventoByIdAndDelete(UUID eventoId) {
        Evento eventoFound = this.findEventoById(eventoId);
        this.eventoRepository.delete(eventoFound);
    }

}
