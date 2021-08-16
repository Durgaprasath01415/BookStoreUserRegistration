package com.userregistration.main.model;

import java.time.LocalDate;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="UserRegistration")
@Data
public class UserRegistrationModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	private int Id;
	private String firstName;
	private String lastName;
	private String kyc;
	private String dateOfBirth;
	private LocalDate registeredDate;
	private LocalDate updatedDate;
	private String emailId;
	private String password;
	private boolean verify;
	private int otp;
	private LocalDate purchaseDate;
	private LocalDate expiryDate;


}
