package com.userregistration.main.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import com.userregistration.main.dto.ResponseDTO;
import com.userregistration.main.dto.UserRegistrationDTO;

@Service
public interface IUserRegistrationService {

	ResponseDTO addUser(UserRegistrationDTO userDTO);

	ResponseDTO getUserById(int id);

	ResponseDTO getAllUser();

	ResponseDTO updateUserById(String token, UserRegistrationDTO userDTO);

	ResponseDTO loginUser(String email, String password);

	ResponseDTO forgotPwd(String email);

	ResponseDTO resetPassword();

	Boolean verify(String token);

	ResponseDTO verifyotp(String token, int otp);

	ResponseDTO delete(String token, int id);

	ResponseDTO purchase(String token);

	ResponseDTO expiry(String token);

	String userName(String token);

	int userId(String token);
	
	ResponseDTO store(String token, MultipartFile kycFile)
			throws IOException;

}
