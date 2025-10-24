package claudiopostiglione.gestionaleEventi.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationExcpetion extends RuntimeException {

    private List<String> errorsMessages;

    public ValidationExcpetion(List<String> errorsMessages) {
        super("Si riportano errori di validazione");
        this.errorsMessages = errorsMessages;
    }
}
