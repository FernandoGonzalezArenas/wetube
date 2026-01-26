package com.wetube.comments.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Object> handleValidationError(MethodArgumentNotValidException ex){
Map<String, String> errores=new HashMap<>();
ex.getBindingResult().getFieldErrors().forEach(error ->
errores.put(error.getField(), error.getDefaultMessage()));
return buildResponse(HttpStatus.BAD_REQUEST, "validacion fallida", errores);
    }

    private ResponseEntity<Object> buildResponse(HttpStatus status, String message, Map<String, String> fieldErrors){
    Map<String, Object> body=new HashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", status.value());
    body.put("error", status.getReasonPhrase());
    body.put("message", message);
if (fieldErrors!=null && !fieldErrors.isEmpty()){
    body.put("fieldErrors", fieldErrors);
}

return new ResponseEntity<Object>(body, status);
}

@ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatus(ResponseStatusException ex){
        return buildResponse((HttpStatus) ex.getStatusCode(), ex.getReason(), null);
}

}
