package com.userregistration.main.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.userregistration.main.dto.ResponseDTO;
import com.userregistration.main.dto.UserRegistrationDTO;
import com.userregistration.main.exceptions.InvalidDetailsException;
import com.userregistration.main.service.IUserRegistrationService;

@RestController
public class UserRegistrationController {
	@Autowired(required = true)
	private IUserRegistrationService userRegistrationService;
	
	@GetMapping("/getall")
	public ResponseEntity<ResponseDTO> getAllContacts() {
		ResponseDTO respDTO = userRegistrationService.getAllUser();
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}
	
	@GetMapping("/getuser/{id}")
	public ResponseEntity<ResponseDTO> getuser(@PathVariable int id) throws InvalidDetailsException {
		ResponseDTO respDTO = userRegistrationService.getUserById(id);
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}
	
	@PostMapping("/registerUser")
	public ResponseEntity<ResponseDTO> registerUser(@RequestBody UserRegistrationDTO userDTO) {
		ResponseDTO respDTO = userRegistrationService.addUser(userDTO);
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}
	
	@PutMapping("/update/{token}")
	public ResponseEntity<ResponseDTO> updateUser(@PathVariable String token,@RequestBody UserRegistrationDTO userDTO)
			throws InvalidDetailsException {
		ResponseDTO respDTO = userRegistrationService.updateUserById(token, userDTO);
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}
	
	@PostMapping("/deleteuser/{token}/{id}")
	public ResponseEntity<ResponseDTO> deleteuser(@PathVariable String token,@PathVariable int id) {
		ResponseDTO respDTO = userRegistrationService.delete(token,id);
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}
	
	@GetMapping("/checkemail/{token}")
	public ResponseEntity<ResponseDTO> check(@PathVariable String token) {
		return new ResponseEntity<ResponseDTO>(
				new ResponseDTO("email verified", userRegistrationService.check(token), 201, "true"),
				HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/loginuser{token}")
	public ResponseEntity<ResponseDTO> loginUser(@PathVariable String token,@RequestParam String email,@RequestParam String password) {
		ResponseDTO respDTO = userRegistrationService.loginUser(token,email,password);
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}

	@PostMapping("/forgotpassword/{token}")
	public ResponseEntity<ResponseDTO> forgotPassword(@PathVariable String token,@RequestParam String email) {
		ResponseDTO respDTO = userRegistrationService.forgotPwd(token,email);
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}
	
	@GetMapping("/resetpassword/{token}")
	public ResponseEntity<ResponseDTO> resetPassword(@PathVariable String token) {
		ResponseDTO respDTO = userRegistrationService.resetPassword(token);
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}
	
	@GetMapping("/verifyemail/{token}")
	public Boolean verifyemail(@PathVariable String token) {
		return userRegistrationService.verify(token);
	}
	
	@GetMapping("/sendotp/{token}")
	public ResponseDTO sendotp(@PathVariable String token) {
		return userRegistrationService.sendotp(token);
	}
	
	@GetMapping("/verifyotp/{token}")
	public ResponseDTO verifyotp(@PathVariable String token ,@RequestParam int otp) {
		return userRegistrationService.verifyotp(token ,otp);
	}
}
