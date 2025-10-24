package claudiopostiglione.gestionaleEventi.payload;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ErrorsDTO(
        String message,
        LocalDate timestamp
) {
}
