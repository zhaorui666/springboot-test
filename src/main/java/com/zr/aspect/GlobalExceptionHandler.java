package com.zr.aspect;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<Map<String, String>> handleValidation(Exception e) {
		Map<String, String> errors = new HashMap<>();
		errors.put("message", e.getMessage());
		return ResponseEntity.badRequest().body(errors);
	}
}
