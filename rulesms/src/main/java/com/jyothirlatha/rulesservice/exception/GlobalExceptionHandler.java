package com.jyothirlatha.rulesservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.jyothirlatha.rulesservice.model.ErrorDetails;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// NullPointerException
	@ExceptionHandler(MinimumBalanceException.class)
	public ResponseEntity<?> minBalance(MinimumBalanceException exception, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	// Access Denied Exception
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> accessDenied(AccessDeniedException exception, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
	}

	// handling global exception
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> globalExceptionHandling(Exception exception, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}