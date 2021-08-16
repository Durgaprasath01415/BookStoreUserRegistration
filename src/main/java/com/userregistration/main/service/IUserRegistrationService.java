package com.userregistration.main.service;

import org.springframework.stereotype.Service;

import com.userregistration.main.dto.ResponseDTO;
import com.userregistration.main.dto.UserRegistrationDTO;
import com.userregistration.main.exceptions.InvalidDetailsException;

@Service
public interface IUserRegistrationService {

	ResponseDTO addUser(UserRegistrationDTO userDTO);

	ResponseDTO getUserById(int id) throws InvalidDetailsException;

	ResponseDTO getAllUser();

	ResponseDTO updateUserById(String token, UserRegistrationDTO userDTO);

	ResponseDTO loginUser(String email, String password) throws InvalidDetailsException;

	ResponseDTO forgotPwd(String email) throws InvalidDetailsException;

	ResponseDTO resetPassword();

	Boolean verify(String token);

	ResponseDTO verifyotp(String token, int otp) throws InvalidDetailsException;

	ResponseDTO delete(String token, int id);

	ResponseDTO purchase(String token);

	ResponseDTO expiry(String token);

}
