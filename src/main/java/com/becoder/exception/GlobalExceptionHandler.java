package com.becoder.exception;

import java.io.FileNotFoundException;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.becoder.util.CommonUtil;

import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger log = (Logger) LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("GlobalExceptionHandler :: handleException :: ", e);
		 return	CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
	@ExceptionHandler(SuccessException.class)
	public ResponseEntity<?> handleSuccessException(SuccessException e) {
		log.error("GlobalExceptionHandler :: handleException ::", e.getMessage());
		return CommonUtil.createBuildResponseMessage(e.getMessage(), HttpStatus.OK);
	}

    
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
		log.error("GlobalExceptionHandler :: handleException ::", e.getMessage());
		return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<?> handleNullPointerException(Exception e){
        log.error("GlobalExceptionHandler :: handleException :: ", e);
		 return	CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	//	return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(Exception e){
        log.error("GlobalExceptionHandler :: handleException :: ", e);
		 return	CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?> handleValidationException(ValidationException e){
		return	CommonUtil.createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ExistDataException.class)
	public ResponseEntity<?> handleExistDataException(ExistDataException e){
		return	CommonUtil.createErrorResponse(e.getMessage(), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<?> handleFileNotFoundException(FileNotFoundException e){
		return	CommonUtil.createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
		return	CommonUtil.createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e){
		return	CommonUtil.createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
