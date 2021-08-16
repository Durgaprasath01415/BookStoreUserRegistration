package com.userregistration.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserRegistrationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String message;
	HttpStatus status;
	private Object data;
	String statusMsg;

	public UserRegistrationException(String message, HttpStatus status, Object data, String statusMsg) {
		super();
		this.message = message;
		this.status = status;
		this.data = data;
		this.statusMsg = statusMsg;
	}

	public UserRegistrationException(int i, String string) {
		// TODO Auto-generated constructor stub
	}

	public UserRegistrationException(String string, HttpStatus ok) {
		// TODO Auto-generated constructor stub
	}

}
