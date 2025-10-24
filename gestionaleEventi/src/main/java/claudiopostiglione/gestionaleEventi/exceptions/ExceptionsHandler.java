package claudiopostiglione.gestionaleEventi.exceptions;

import claudiopostiglione.gestionaleEventi.payload.ErrorsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;

@RestControllerAdvice
public class ExceptionsHandler extends RuntimeException {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorsDTO handleUnauthorizedException(UnauthorizedException ex){
        return new ErrorsDTO(ex.getMessage(), LocalDate.now());
    }

}
