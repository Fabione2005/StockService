package com.client.bridge.controller.exceptionHandler;

import java.security.SignatureException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.client.bridge.exception.CommonException;
import com.client.bridge.exception.GenericException;
import com.client.bridge.model.generic.BaseResultDTO;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { GenericException.class, Exception.class, SignatureException.class })
	protected ResponseEntity<Object> handleConflict(RuntimeException exception, WebRequest request) {
		BaseResultDTO resultMessage = new BaseResultDTO();
		HttpStatus httpStatus = null;
		if (exception instanceof CommonException) {
			GenericException exceptionResult = (GenericException) exception;
			httpStatus = exceptionResult.getStatus();
			resultMessage = new BaseResultDTO(String.valueOf(httpStatus.value()),exceptionResult.getMessage());
		} 
		else {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			resultMessage = new BaseResultDTO(String.valueOf(httpStatus.value()),"ERROR DESCONOCIDO DE SERVIDOR");
		}
		logger.error(resultMessage.getResponseDescription());
		return handleExceptionInternal(exception, resultMessage, new HttpHeaders(), httpStatus, request);
	}

}