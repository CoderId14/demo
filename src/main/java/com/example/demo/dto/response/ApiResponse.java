package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

@Data
public class ApiResponse implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = 9L;

	@JsonProperty("success")
	private Boolean success;

	@JsonProperty("message")
	private String message;

	@JsonProperty("messages")
	private List<String> messages;


	@JsonIgnore
	private HttpStatus status;

	public ApiResponse() {

	}

	public ApiResponse(Boolean success, String message) {
		this.success = success;
		this.message = message;
	}
	public ApiResponse(Boolean success, List<String> messages) {
		this.success = success;
		this.messages = messages;
	}


	public ApiResponse(Boolean success, String message, HttpStatus httpStatus) {
		this.success = success;
		this.message = message;
		this.status = httpStatus;
	}
}
