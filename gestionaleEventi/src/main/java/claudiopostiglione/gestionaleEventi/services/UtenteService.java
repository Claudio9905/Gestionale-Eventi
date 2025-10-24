package claudiopostiglione.gestionaleEventi.services;

import claudiopostiglione.gestionaleEventi.entities.Utente;
import claudiopostiglione.gestionaleEventi.exceptions.BadRequestException;
import claudiopostiglione.gestionaleEventi.exceptions.IdNotFoundException;
import claudiopostiglione.gestionaleEventi.exceptions.NotFoundException;
import claudiopostiglione.gestionaleEventi.payload.UtenteDTO;
import claudiopostiglione.gestionaleEventi.repositories.UtenteRepository;
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
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;


    //OPERAZIONI CRUD per gli utenti

    // 1. per la chiamata GET che restituisce la lista degli utenti
    public Page<Utente> findAllDipendenti(int numPage, int sizePage, String sortBy) {
        if (sizePage > 30) sizePage = 30;
        sortBy = "nome";
        Pageable pageable = PageRequest.of(numPage, sizePage, Sort.by(sortBy).ascending());
        return this.utenteRepository.findAll(pageable);
    }
    
    // 2. per la chiamata GET per un singolo utente
    public Utente findUtenteById(UUID utenteId) {
        return this.utenteRepository.findById(utenteId).orElseThrow(() -> new IdNotFoundException("L'utente con ID: " + utenteId + " non è stato trovato"));
    }

    // 3. per la chiamata PUT per la modifica dell'utente
    public Utente findUtenteByIdAndUpdate(UUID utenteId, UtenteDTO body){

        Utente utenteFound  = this.findUtenteById(utenteId);

        if(!utenteFound.getEmail().equals(body.email())){
           this.utenteRepository.findByEmail(body.email()).ifPresent(utente -> {
               throw new BadRequestException("L'email " + utente.getEmail() + " esiste già");
           });
        }

        utenteFound.setNome(body.nome());
        utenteFound.setCognome(body.cognome());
        utenteFound.setEta(body.eta());
        utenteFound.setEmail(body.email());
        utenteFound.setAvatar_url("https://ui-avatars.com/api/?name=" + body.nome() + "+" + body.cognome());
        utenteFound.setRole(body.role());

        Utente updateUtente = this.utenteRepository.save(utenteFound);
        log.info("L'utente " + updateUtente.getNome() + " " + updateUtente.getCognome() + " con ID: " + updateUtente.getId() + " è stato aggiornato correttamente");

        return updateUtente;
    }

    // per la chiamata DELETE di un utente
    public void findUtenteByIdAndDelete(UUID utenteId){
        Utente utenteFound = this.findUtenteById(utenteId);
        this.utenteRepository.delete(utenteFound);
    }


    public Utente findUtenteByEmail(String email){
        return this.utenteRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("L'email " + email + " non è stata trovata"));
    }

}
