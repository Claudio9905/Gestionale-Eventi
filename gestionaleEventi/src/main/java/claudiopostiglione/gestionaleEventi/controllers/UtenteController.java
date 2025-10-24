package claudiopostiglione.gestionaleEventi.controllers;

import claudiopostiglione.gestionaleEventi.entities.Utente;
import claudiopostiglione.gestionaleEventi.payload.UtenteDTO;
import claudiopostiglione.gestionaleEventi.services.UtenteService;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

// URL: http://localhost:3001/....
@RestController
@RequestMapping("/utenti")
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    //Endpoint "/me"
    @GetMapping("/me")
    public Utente findMe(@AuthenticationPrincipal Utente currentUtente) {
        return currentUtente;
    }

    @PutMapping("/me")
    public Utente findMeAndUpdate(@AuthenticationPrincipal Utente currentUtente, @RequestBody UtenteDTO bodyUtente) {
        return this.utenteService.findUtenteByIdAndUpdate(currentUtente.getId(), bodyUtente);
    }

    @DeleteMapping("/me")
    public void findMeAndDelete(@AuthenticationPrincipal Utente currentUtente) {
        this.utenteService.findUtenteByIdAndDelete(currentUtente.getId());
    }

    @PatchMapping("/me/avatarUrl")
    public Utente updateMyAvatar(@AuthenticationPrincipal Utente currentUtente, @RequestParam("avatarUrl")MultipartFile file){
        System.out.println("| Nome del file: " + file.getName());
        System.out.println("| Dimensione del file: " + file.getSize());
        return this.utenteService.uploadAvatarProfile(file,currentUtente.getId());
    }

    //Endpoint "/utenti/..."
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Utente> getAllUtenti(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
        return this.utenteService.findAllDipendenti(page, size, sortBy);
    }

    @DeleteMapping("/{utenteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(("hasAuthority('ADMIN')"))
    public void getUtenteAndDelete(@PathVariable UUID utenteId){
        this.utenteService.findUtenteByIdAndDelete(utenteId);
    }


}
