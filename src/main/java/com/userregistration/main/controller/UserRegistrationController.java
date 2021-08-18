package com.userregistration.main.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.userregistration.main.dto.ResponseDTO;
import com.userregistration.main.dto.UserRegistrationDTO;
import com.userregistration.main.service.IUserRegistrationService;

import io.swagger.annotations.ApiOperation;

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
	public ResponseEntity<ResponseDTO> getuser(@PathVariable String token,@PathVariable int id){
		ResponseDTO respDTO = userRegistrationService.getUserById(id);
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}

	@PostMapping("/registerUser")
	public ResponseEntity<ResponseDTO> registerUser(@RequestBody UserRegistrationDTO userDTO) {
		ResponseDTO respDTO = userRegistrationService.addUser(userDTO);
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}

	@PutMapping("/update/{token}")
	public ResponseEntity<ResponseDTO> updateUser(@PathVariable String token, @RequestBody UserRegistrationDTO userDTO){
		ResponseDTO respDTO = userRegistrationService.updateUserById(token, userDTO);
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}

	@DeleteMapping("/deleteuser/{token}/{id}")
	public ResponseEntity<ResponseDTO> deleteuser(@PathVariable String token, @PathVariable int id) {
		ResponseDTO respDTO = userRegistrationService.delete(token, id);
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}

	@PostMapping("/loginuser")
	public ResponseEntity<ResponseDTO> loginUser(@RequestParam String email,
			@RequestParam String password){
		ResponseDTO respDTO = userRegistrationService.loginUser(email, password);
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}

	@PostMapping("/forgotpassword")
	public ResponseEntity<ResponseDTO> forgotPassword(@RequestParam String email) {
		ResponseDTO respDTO = userRegistrationService.forgotPwd(email);
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}

	@GetMapping("/resetpassword")
	public ResponseEntity<ResponseDTO> resetPassword() {
		ResponseDTO respDTO = userRegistrationService.resetPassword();
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}

	@GetMapping("/verifyemail/{token}")
	public Boolean verifyemail(@PathVariable String token) {
		return userRegistrationService.verify(token);
	}

	@GetMapping("/verifyotp/{token}")
	public ResponseDTO verifyotp(@PathVariable String token, @RequestParam int otp) {
		return userRegistrationService.verifyotp(token, otp);
	}
	
	@PutMapping("/purchase/{token}")
	public ResponseEntity<ResponseDTO> purchase(@PathVariable String token){
		ResponseDTO respDTO = userRegistrationService.purchase(token);
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}
	
	@PutMapping("/expiry/{token}")
	public ResponseEntity<ResponseDTO> expiry(@PathVariable String token){
		ResponseDTO respDTO = userRegistrationService.expiry(token);
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}
	
	@GetMapping("/userid/{token}")
	public int userId(@PathVariable String token) {
		return userRegistrationService.userId(token);
	}
	
	@PostMapping(value = "/uploadkyc/{token}/", consumes = { "multipart/form-data" })
	@ApiOperation(value = "Upload Documents", response = ResponseDTO.class)
	public ResponseEntity<ResponseDTO> addBankDetail(@PathVariable String token,
			@RequestParam("kycFile") MultipartFile kycFile) throws IOException{
		ResponseDTO respDTO = userRegistrationService.store(token, kycFile);
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}
}
