package com.userregistration.main.service;

import org.springframework.stereotype.Service;

import com.userregistration.main.dto.ResponseDTO;
import com.userregistration.main.dto.UserRegistrationDTO;
import com.userregistration.main.exceptions.InvalidDetailsException;
import com.userregistration.main.model.UserRegistrationModel;

@Service
public interface IUserRegistrationService {

	ResponseDTO addUser(UserRegistrationDTO userDTO);
	
	ResponseDTO getUserById(int id) throws InvalidDetailsException;
	
	ResponseDTO getAllUser();

	ResponseDTO updateUserById(String token,UserRegistrationDTO userDTO);

	ResponseDTO loginUser(String token,String email,String password);

	ResponseDTO forgotPwd(String token,String email);
	
	ResponseDTO resetPassword(String token);

	Boolean verify(String token);

	ResponseDTO sendotp(String token);

	ResponseDTO verifyotp(String token ,int otp);

	UserRegistrationModel check(String token);

	ResponseDTO delete(String token, int id);

}
