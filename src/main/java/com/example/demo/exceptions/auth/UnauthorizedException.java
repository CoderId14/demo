package com.example.demo.exceptions.auth;


import com.example.demo.dto.response.ApiResponse;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private ApiResponse apiResponse;

	private String message;

	public UnauthorizedException(ApiResponse apiResponse) {
		super();
		this.apiResponse = apiResponse;
	}

	public UnauthorizedException(String message) {
		super(message);
		this.message = message;
	}

	public UnauthorizedException(String message, Throwable cause) {
		super(message, cause);
	}



}
