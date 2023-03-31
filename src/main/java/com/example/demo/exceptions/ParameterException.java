package com.example.demo.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ParameterException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String fieldName;

	public ParameterException(String fieldName) {
		super(String.format("%s param is require",fieldName));
		this.fieldName = fieldName;
	}

}
