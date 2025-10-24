package claudiopostiglione.gestionaleEventi.exceptions;

import claudiopostiglione.gestionaleEventi.payload.ErrorsDTO;
import claudiopostiglione.gestionaleEventi.payload.ErrorsWithListDTO;
import jakarta.persistence.Enumerated;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;

@RestControllerAdvice
public class ExceptionsHandler extends RuntimeException {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorsDTO handleUnauthorizedException(UnauthorizedException ex) {
        return new ErrorsDTO(ex.getMessage(), LocalDate.now());
    }

    @ExceptionHandler(IdNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsDTO handleIdNotFoundException(IdNotFoundException ex) {
        return new ErrorsDTO(ex.getMessage(), LocalDate.now());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsDTO handleBadRequestException(BadRequestException ex) {
        return new ErrorsDTO(ex.getMessage(), LocalDate.now());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsDTO handleNotFoundException(NotFoundException ex) {
        return new ErrorsDTO(ex.getMessage(), LocalDate.now());
    }

    @ExceptionHandler(ValidationExcpetion.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsWithListDTO handleValidationException(ValidationExcpetion ex){
        return new ErrorsWithListDTO(ex.getMessage(), LocalDate.now(), ex.getErrorsMessages());
    }
}
