package claudiopostiglione.gestionaleEventi.payload;

import claudiopostiglione.gestionaleEventi.entities.RuoloUtente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UtenteDTO(
        @NotBlank(message = "Il nome è obbligatorio")
        @Size(min = 2, max = 20, message = "Il nome deve avere un minimo di due caratteri e un massimo di 20")
        String nome,
        @NotBlank(message = "Il cognome è obbligatorio")
        @Size(min = 2, max = 20, message = "Il cognome deve avere un minimo di due caratteri e un massimo di 20")
        String cognome,
        @NotNull(message = "L'età non può essere nulla")
        int eta,
        @NotBlank(message = "L'indirizzo e-mail è obbligatorio")
        @Email(message = "l'indirizzo e-mail inserito non è del formato corretto")
        String email,
        @NotBlank(message = "La password è obbligatoria")
        @Size(min = 8, message = "La password deve avere un minimo di 8 caratteri")
        String password,
        RuoloUtente role
) {
}
