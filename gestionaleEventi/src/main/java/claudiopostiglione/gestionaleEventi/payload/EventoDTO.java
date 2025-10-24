package claudiopostiglione.gestionaleEventi.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record EventoDTO(
        @NotBlank(message = "Il titolo è obbligatorio")
        @Size(min = 4, max = 20, message = "Il titolo deve avere un minimo di 4 caratteri e un massimo di 20")
        String titolo,
        @NotBlank(message = "La descrizione è obbligatoria")
        @Size(min = 20, max = 400, message = "La descrizione deve avere un minimo di 100 caratteri e un massimo di 400")
        String descrizione,
        @NotNull(message = "La data dell'evento non può essere nulla")
        LocalDate dataEvento,
        @NotBlank(message = "Il luogo dell'evento è obbligatorio")
        @Size(min = 2, max = 20, message = "Il luogo deve avere un minimo di 2 caratteri e un massimo di 20")
        String luogo,
        @NotNull(message = "Il numero di posti non può essere nullo")
        int numPostDisp,
        @NotNull(message = "L'ID dell'utente non può essere nullo")
        UUID utenteId
) {
}
