package com.jlr.ddc.servicebase.controller.vehicle;

import com.jlr.ddc.servicebase.repository.vehicle.VehicleNotFoundException;

import java.util.Optional;

import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequestMapping(produces = "application/vnd.error+json")
public class VehicleControllerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(VehicleNotFoundException.class)
  public ResponseEntity<VndErrors> notFoundException(VehicleNotFoundException e) {
    return error(e, HttpStatus.NOT_FOUND, e.getId().toString());
  }

  private ResponseEntity<VndErrors> error(
      Exception exception,
      HttpStatus httpStatus,
      String logRef) {
    
    final String message = 
        Optional.of(exception.getMessage()).orElse(exception.getClass().getSimpleName());
    
    return new ResponseEntity<>(new VndErrors(logRef, message), httpStatus);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<VndErrors> assertionException(IllegalArgumentException e) {
    return error(e, HttpStatus.NOT_FOUND, e.getLocalizedMessage());
  }
  
}
