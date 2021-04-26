package com.davidlinhares.medicalcheck.resource.exception;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.davidlinhares.medicalcheck.service.exception.DatabaseException;
import com.davidlinhares.medicalcheck.service.exception.IntegrationViolationDataBaseException;
import com.davidlinhares.medicalcheck.service.exception.ResourceNotFoundException;

@RestControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
		String error = "Resoucer Not Found";
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI(), e.getErros());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> dataBase(DatabaseException e, HttpServletRequest request) {
		String error = "Database error";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI(), e.getErros() );
		return ResponseEntity.status(status).body(err);
	}
	@ExceptionHandler(IntegrationViolationDataBaseException.class)
	public ResponseEntity<StandardError> integrationDataBase(IntegrationViolationDataBaseException e, HttpServletRequest request) {
		String error = "Error de integridade dos dados";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI(), e.getErros() );
		return ResponseEntity.status(status).body(err);
	}
	@ExceptionHandler(IlegalFormatFile.class)
	public ResponseEntity<StandardError> integrationDataBase(IlegalFormatFile e, HttpServletRequest request) {
		String error = "Formato do arquivo não permitido:" + e.getMessage();
		HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI(), null );
		return ResponseEntity.status(status).body(err);
	}
}
