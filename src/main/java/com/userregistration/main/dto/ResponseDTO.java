package com.userregistration.main.dto;

import com.userregistration.main.model.UserRegistrationModel;

import lombok.Data;

@Data
public class ResponseDTO {

	private String message;
	private Object data;
	
	public ResponseDTO(String string, UserRegistrationModel userRegistrationModel, int i, String string2) {

	}
	public ResponseDTO(String message, Object data) {
		this.message = message;
		this.data = data;
	}
	public ResponseDTO(String string) {
		// TODO Auto-generated constructor stub
	}
}
