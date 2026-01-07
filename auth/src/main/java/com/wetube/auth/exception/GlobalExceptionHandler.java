package com.wetube.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex){
        return buildResponse(HttpStatus.UNAUTHORIZED, "acceso no autorizado al recurso solicitado", null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex){
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "error interno"+ ex.getMessage(), null);
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

    return new ResponseEntity<>(body, status);
}

}
