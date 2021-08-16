package com.userregistration.main.dto;

import lombok.Data;

@Data
public class UserRegistrationDTO {
	
	private String firstName;
	private String lastName;
	private String kyc;
	private String dateOfBirth;
	private String emailId;
	private String password;
}
