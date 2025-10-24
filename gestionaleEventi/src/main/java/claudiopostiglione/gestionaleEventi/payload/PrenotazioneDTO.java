package claudiopostiglione.gestionaleEventi.payload;

import claudiopostiglione.gestionaleEventi.entities.Evento;
import claudiopostiglione.gestionaleEventi.entities.Utente;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record PrenotazioneDTO(
        @NotNull(message = "La data della prenotazione non deve essere nulla")
        LocalDate dataPrenotazione,
        @NotNull(message = "L'ID dell'utente non deve essere nullo")
        UUID utenteId,
        @NotNull(message = "L'ID dell'evento non deve essere nullo")
        UUID eventoId
) {
}
