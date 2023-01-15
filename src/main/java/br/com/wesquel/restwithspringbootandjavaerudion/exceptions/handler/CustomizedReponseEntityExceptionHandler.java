package br.com.wesquel.restwithspringbootandjavaerudion.exceptions.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.wesquel.restwithspringbootandjavaerudion.exceptions.ExceptionReponse;
import br.com.wesquel.restwithspringbootandjavaerudion.exceptions.RequiredObjectIsNullException;
import br.com.wesquel.restwithspringbootandjavaerudion.exceptions.ResourceNotFoundException;

@ControllerAdvice
@RestController
public class CustomizedReponseEntityExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionReponse> handleAllExceptions(Exception ex, WebRequest request){
        ExceptionReponse exceptionReponse = new ExceptionReponse(
            new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionReponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionReponse> handleNotFoundExceptions(Exception ex, WebRequest request){
        ExceptionReponse exceptionReponse = new ExceptionReponse(
            new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionReponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RequiredObjectIsNullException.class)
    public final ResponseEntity<ExceptionReponse> handleBadRequestExceptions(Exception ex, WebRequest request){
        ExceptionReponse exceptionReponse = new ExceptionReponse(
            new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionReponse, HttpStatus.BAD_REQUEST);
    }
}
