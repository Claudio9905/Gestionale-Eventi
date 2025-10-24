package claudiopostiglione.gestionaleEventi.services;

import claudiopostiglione.gestionaleEventi.entities.Evento;
import claudiopostiglione.gestionaleEventi.entities.Prenotazione;
import claudiopostiglione.gestionaleEventi.entities.Utente;
import claudiopostiglione.gestionaleEventi.exceptions.BadRequestException;
import claudiopostiglione.gestionaleEventi.exceptions.IdNotFoundException;
import claudiopostiglione.gestionaleEventi.payload.PrenotazioneDTO;
import claudiopostiglione.gestionaleEventi.repositories.EventoRepository;
import claudiopostiglione.gestionaleEventi.repositories.PrenotazioneRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@Transactional
public class PrenotazioneService {
    @Autowired
    private PrenotazioneRepository prenotazioneRepository;
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private EventoService eventoService;
    @Autowired
    private EventoRepository eventoRepository;

    //OPERAZIONI CRUD per le prenotazioni

    // 1. per la chiamata GET che restituisce la lista delle prenotazioni
    public Page<Prenotazione> findAllPrenotazioni(int numPage, int sizePage, String sortBy) {
        if (sizePage > 30) sizePage = 30;
        Pageable pageable = PageRequest.of(numPage, sizePage, Sort.by(sortBy).ascending());
        return this.prenotazioneRepository.findAll(pageable);
    }

    // 2. per la chiamata POST che crea la prenotazione
    public Prenotazione createPrenotazione(PrenotazioneDTO body) {
        Utente utenteFound = this.utenteService.findUtenteById(body.utenteId());
        Evento eventoFound = this.eventoService.findEventoById(body.eventoId());

        if (!body.dataPrenotazione().equals(eventoFound.getDataEvento())) {
            throw new BadRequestException("Impossibile prenotare, la data inserita per la prenotazione non corrisponde alla data dell'evento, oppure è stata già fatta la prenotazione");
        }
        if (eventoFound.getNumPostDisp() == 0) {
            throw new BadRequestException("Impossibile prenotare, posti esauriti");
        }


        Prenotazione newPrenotazione = new Prenotazione(body.dataPrenotazione(), utenteFound, eventoFound);
        utenteFound.getListaPrenotazioni().add(newPrenotazione);
        eventoFound.getListaPrenotazioni().add(newPrenotazione);

        eventoFound.setNumPostDisp(eventoFound.getNumPostDisp() - 1);
        System.out.println(eventoFound.getNumPostDisp());

        this.eventoRepository.save(eventoFound);
        this.prenotazioneRepository.save(newPrenotazione);

        return newPrenotazione;

    }

    // 3. per la chiamata GET per una singola prenotazione
    public Prenotazione findPrenotazioneById(UUID prenotazioneId) {
        return this.prenotazioneRepository.findById(prenotazioneId).orElseThrow(() -> new IdNotFoundException("La prenotazione con ID: " + prenotazioneId + " non è stato trovato"));
    }

    // 4. per la chiamata PUT per la modifica della prenotazione
    public Prenotazione findPrenotazioneByIdAndUpdate(UUID prenotazioneId, PrenotazioneDTO body) {

        Prenotazione prenotazioneFound = this.findPrenotazioneById(prenotazioneId);

        prenotazioneFound.setDataPrenotazione(body.dataPrenotazione());

        Prenotazione updatePrenotazione = this.prenotazioneRepository.save(prenotazioneFound);
        log.info("La prenotazione con ID: " + updatePrenotazione.getId() + " è stata aggiornata correttamente");
        return updatePrenotazione;
    }

    // 5. per la chiamata DELETE di una prenotazione
    public void findPrenotazioneByIdAndDelete(UUID prenotazioneId, Utente currentUtente) {

        Utente utenteFound = this.utenteService.findUtenteById(currentUtente.getId());

        Prenotazione prenotazioneFound = this.findPrenotazioneById(prenotazioneId);
        if (!utenteFound.getId().equals(prenotazioneFound.getUtente().getId()))
            throw new BadRequestException("Attenzione, l'ID che hai inserito non è il tuo!");

        this.prenotazioneRepository.delete(prenotazioneFound);

        int resetNumeroPostiEvento = prenotazioneFound.getEvento().getNumPostDisp();
        resetNumeroPostiEvento++;
        Evento evento = prenotazioneFound.getEvento();
        evento.setNumPostDisp(resetNumeroPostiEvento);
        this.eventoRepository.save(evento);

    }
}
