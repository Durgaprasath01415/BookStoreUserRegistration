package com.userregistration.main.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.userregistration.main.dto.ResponseDTO;
import com.userregistration.main.dto.UserRegistrationDTO;
import com.userregistration.main.exceptions.UserRegistrationException;
import com.userregistration.main.model.UserRegistrationModel;
import com.userregistration.main.repository.UserRegistrationRepository;
import com.userregistration.main.utli.JMSUtli;
import com.userregistration.main.utli.TokenUtli;

@Service
public class UserRegistrationServiceImpl implements IUserRegistrationService {

	@Autowired(required = true)
	UserRegistrationRepository userrepository;
	@Autowired(required = true)
	TokenUtli tokenutli;
	@Autowired(required = true)
	ModelMapper modelmapper;
	
	@Override
	public ResponseDTO getAllUser() {
		List<UserRegistrationModel> isUserPresent = userrepository.findAll();
		return new ResponseDTO("List of All Users", isUserPresent);
	}

	@Override
	public ResponseDTO getUserById(int id) {
		Optional<UserRegistrationModel> isUserPresent = userrepository.findById(id);
		return new ResponseDTO("User of id:" + id, isUserPresent);
	}
	
	@Override
	public ResponseDTO addUser(UserRegistrationDTO userDTO) {
		Optional<UserRegistrationModel> isUserPresent = userrepository.findByEmailId(userDTO.getEmailId());
		if(!isUserPresent.isPresent()) {
		UserRegistrationModel createUser = modelmapper.map(userDTO, UserRegistrationModel.class);
		createUser.setRegisteredDate(LocalDate.now());
		Random rndNumber = new Random();
		int otpNumber = rndNumber.nextInt(999999);
		createUser.setOtp(otpNumber);
		String body = "OTP = " + createUser.getOtp();
		JMSUtli.sendEmail(createUser.getEmailId(), "OTP(One Time Password)", body);
		System.out.println(body);
		userrepository.save(createUser);
		return new ResponseDTO("User is ADDED", userDTO);
	}else {
		throw new UserRegistrationException("User is already present", HttpStatus.OK, isUserPresent.get(), "false");
	}
	}

	@Override
	public ResponseDTO updateUserById(String token, UserRegistrationDTO userDTO) {
		int Id = tokenutli.decodeToken(token);
		Optional<UserRegistrationModel> updateUser = userrepository.findById(Id);
		if (updateUser.isPresent()) {
			updateUser.get().setFirstName(userDTO.getFirstName());
			updateUser.get().setLastName(userDTO.getLastName());
			updateUser.get().setKyc(userDTO.getKyc());
			updateUser.get().setDateOfBirth(userDTO.getDateOfBirth());
			updateUser.get().setEmailId(userDTO.getEmailId());
			updateUser.get().setPassword(userDTO.getPassword());
			userrepository.save(updateUser.get());
			return new ResponseDTO("Update of user details is successfull", updateUser);
		} else {
			throw new UserRegistrationException("User id is not present", HttpStatus.OK, updateUser.get(), "false");
		}
	}

	@Override
	public ResponseDTO delete(String token, int id) {
		int Id = tokenutli.decodeToken(token);
		Optional<UserRegistrationModel> isUserPresent = userrepository.findById(Id);
		if (isUserPresent.isPresent()) {
			userrepository.delete(isUserPresent.get());
			return new ResponseDTO("User Data Deleted successfull", isUserPresent.get().getId());
		} else {
			throw new UserRegistrationException("User id is not present", HttpStatus.OK, isUserPresent.get(), "false");
		}
	}

	@Override
	public ResponseDTO loginUser( String email, String password){
		Optional<UserRegistrationModel> isUserPresent = userrepository.findByEmailId(email);
		if (isUserPresent.isPresent()) {
			if (isUserPresent.get().getEmailId().equals(email) && isUserPresent.get().getPassword().equals(password)) {
				String tokenForLogin = tokenutli.createToken(isUserPresent.get().getId());
				return new ResponseDTO("Login is successfull", "Token :" + tokenForLogin);
			} else {
				throw new UserRegistrationException("Login failed", HttpStatus.OK, isUserPresent.get(), "false");
			}
		} else {
			throw new UserRegistrationException("User id is not present", HttpStatus.OK, isUserPresent.get(), "false");
		}
	}

	@Override
	public ResponseDTO forgotPwd(String email){
		Optional<UserRegistrationModel> isUserPresent = userrepository.findByEmailId(email);
		if (isUserPresent.isPresent()) {
			if (isUserPresent.get().getEmailId().equals(email)) {
				String body = "http://localhost:8080/resetpassword/"
						+ tokenutli.createToken(isUserPresent.get().getId());
				JMSUtli.sendEmail(isUserPresent.get().getEmailId(), "Reset Password", body);
				return new ResponseDTO("Password is reset", body);
			} else {
				throw new UserRegistrationException("Email id is incorrect", HttpStatus.OK, isUserPresent.get(), "false");
			}
		} else {
			throw new UserRegistrationException("User id is not present", HttpStatus.OK, null, "false");
		}

	}

	@Override
	public ResponseDTO resetPassword() {
		return new ResponseDTO("Password is resetted successfully", null);
	}

	@Override
	public Boolean verify(String token){
		int Id = tokenutli.decodeToken(token);
		Optional<UserRegistrationModel> verifyUser = userrepository.findById(Id);
		if (verifyUser != null) {
			return true;
			
		} else {
			return false;
		}
	}

	@Override
	public ResponseDTO verifyotp(String token, int otp){
		int Id = tokenutli.decodeToken(token);
		Optional<UserRegistrationModel> isUserPresent = userrepository.findById(Id);
		if (isUserPresent.isPresent()) {
			if (isUserPresent.get().getOtp() == otp) {
				isUserPresent.get().setVerify(true);
				userrepository.save(isUserPresent.get());
				return new ResponseDTO("OTP is Verifed");
			} else {
				throw new UserRegistrationException("OTP is incorrect", HttpStatus.OK, isUserPresent.get(), "false");
			}
		} else {
			throw new UserRegistrationException("User id is not present", HttpStatus.OK, isUserPresent.get(), "false");
		}
	}

	@Override
	public ResponseDTO purchase(String token) {
		int Id = tokenutli.decodeToken(token);
		Optional<UserRegistrationModel> isUserPresent = userrepository.findById(Id);
		if (isUserPresent.isPresent()) {
			LocalDate today = LocalDate.now();
			isUserPresent.get().setPurchaseDate(LocalDate.now());
			isUserPresent.get().setExpiryDate(today.plusYears(1));
			String body = "Dear User you have successfully purchased";
			JMSUtli.sendEmail(isUserPresent.get().getEmailId(), "Successfully purchased ", body);
			userrepository.save(isUserPresent.get());
			return new ResponseDTO("User is purchase is successfull","ExpiryDate : " +isUserPresent.get().getExpiryDate());
		}else {
			throw new UserRegistrationException("User id is not present", HttpStatus.OK, isUserPresent.get(), "false");
		}
	}

	@Override
	public ResponseDTO expiry(String token) {
		int Id = tokenutli.decodeToken(token);
		Optional<UserRegistrationModel> isUserPresent = userrepository.findById(Id);
		if (isUserPresent.isPresent()) {
			LocalDate today = LocalDate.now();
			if(today.equals(isUserPresent.get().getExpiryDate())) {
				String body = "Dear User your purchase of the book is getting expired,Please get subscription to keep reading the book";
				JMSUtli.sendEmail(isUserPresent.get().getEmailId(), "Remaindar of purchase is getting expired", body);
				return new ResponseDTO("Mail remaindar is sent to ",isUserPresent.get().getFirstName());
			}else {
				throw new UserRegistrationException("Mail remaindar is not sent to ", HttpStatus.OK, isUserPresent.get(), "false");
			}
		}else {
			throw new UserRegistrationException("User id is not present", HttpStatus.OK, isUserPresent.get(), "false");
			}
	}
}

