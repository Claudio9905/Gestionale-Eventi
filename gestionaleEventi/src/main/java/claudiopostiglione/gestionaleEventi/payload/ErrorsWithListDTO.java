package claudiopostiglione.gestionaleEventi.payload;

import java.time.LocalDate;
import java.util.List;

public record ErrorsWithListDTO(
        String message,
        LocalDate timestamp,
        List<String> errorsMessages
) {
}
