package claudiopostiglione.gestionaleEventi.services;

import claudiopostiglione.gestionaleEventi.entities.Utente;
import claudiopostiglione.gestionaleEventi.exceptions.BadRequestException;
import claudiopostiglione.gestionaleEventi.exceptions.IdNotFoundException;
import claudiopostiglione.gestionaleEventi.exceptions.NotFoundException;
import claudiopostiglione.gestionaleEventi.payload.UtenteDTO;
import claudiopostiglione.gestionaleEventi.repositories.UtenteRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private PasswordEncoder bCrypt;
    @Autowired
    private Cloudinary imageUploader;

    //Questi attributi mi serviranno per controllare alcuni parametri del file
    private static final long MAX_SIZE = 5 * 1024 * 1024; // corrispondono a 5MB
    private static final List<String> ALLOWED_FORMAT = List.of("image/jpeg", "image/png");

    //OPERAZIONI CRUD per gli utenti

    // 1. per la chiamta POST per la registrazione di un utente
    public Utente createUtente(UtenteDTO body) {

        //Controllo se c'è già un utente con l'email inserita
        this.utenteRepository.findByEmail(body.email()).ifPresent(utente -> {
            throw new BadRequestException("L'email " + utente.getEmail() + " esiste già");
        });

        Utente newUtente = new Utente(body.nome(), body.cognome(), body.eta(), body.email(), bCrypt.encode(body.password()), body.role());
        newUtente.setAvatar_url("https://ui-avatars.com/api/?name=" + body.nome() + "+" + body.cognome());
        this.utenteRepository.save(newUtente);
        log.info("L'utente " + newUtente.getNome() + " " + newUtente.getCognome() + " con ID: " + newUtente.getId() + " è stato salvato correttamente");

        return newUtente;
    }

    // 2. per la chiamata GET che restituisce la lista degli utenti
    public Page<Utente> findAllDipendenti(int numPage, int sizePage, String sortBy) {
        if (sizePage > 30) sizePage = 30;
        sortBy = "nome";
        Pageable pageable = PageRequest.of(numPage, sizePage, Sort.by(sortBy).ascending());
        return this.utenteRepository.findAll(pageable);
    }

    // 3. per la chiamata GET per un singolo utente
    public Utente findUtenteById(UUID utenteId) {
        return this.utenteRepository.findById(utenteId).orElseThrow(() -> new IdNotFoundException("L'utente con ID: " + utenteId + " non è stato trovato"));
    }

    // 4. per la chiamata PUT per la modifica dell'utente
    public Utente findUtenteByIdAndUpdate(UUID utenteId, UtenteDTO body) {

        Utente utenteFound = this.findUtenteById(utenteId);

        if (!utenteFound.getEmail().equals(body.email())) {
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

    // 5. per la chiamata DELETE di un utente
    public void findUtenteByIdAndDelete(UUID utenteId) {
        Utente utenteFound = this.findUtenteById(utenteId);
        this.utenteRepository.delete(utenteFound);
    }

    // 6. per la chiamata PATCH per l'upload delle immagini
    public Utente uploadAvatarProfile(MultipartFile file, UUID utenteId) {

        if (file.isEmpty()) throw new BadRequestException("File vuoto!");
        if (file.getSize() > MAX_SIZE)
            throw new BadRequestException("La dimensione del file è troppo grande, il limite è di 5MB");
        if (!(ALLOWED_FORMAT.contains(file.getContentType())))
            throw new BadRequestException("Attenzione, il formato del file è sbagliato. Formato file acconsentito: .jpeg, .png");

        Utente utenteFound = this.findUtenteById(utenteId);

        try {
            //Cattura dell'URL dell'immagine
            Map resultMap = imageUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageURL = (String) resultMap.get("url");

            //Salvataggio dell'immagine catturata
            utenteFound.setAvatar_url(imageURL);
            this.utenteRepository.save(utenteFound);
            return utenteFound;
        } catch (Exception e) {
            throw new BadRequestException("Errore nell'upload dell'immagine");
        }

    }

    public Utente findUtenteByEmail(String email) {
        return this.utenteRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("L'email " + email + " non è stata trovata"));
    }

}
