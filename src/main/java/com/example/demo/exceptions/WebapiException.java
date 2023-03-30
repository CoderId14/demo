package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class WebapiException extends RuntimeException {

	private static final long serialVersionUID = 7L;

	private final HttpStatus status;
	private final String message;

	public WebapiException(HttpStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public WebapiException(HttpStatus status, String message, Throwable exception) {
		super(exception);
		this.status = status;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

}
