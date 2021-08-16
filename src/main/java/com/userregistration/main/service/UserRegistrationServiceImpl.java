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
import com.userregistration.main.exceptions.InvalidDetailsException;
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
	public ResponseDTO addUser(UserRegistrationDTO userDTO) {
//		Optional<UserRegistrationModel> isUserPresent = userrepository.findByemail(userDTO.getEmailId());
//		if(!isUserPresent.isPresent()) {
		UserRegistrationModel createUser = modelmapper.map(userDTO, UserRegistrationModel.class);
		createUser.setRegisteredDate(LocalDate.now());
		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		createUser.setOtp(number);
		userrepository.save(createUser);
		String token = tokenutli.createToken(createUser.getId());
		System.out.println(token);
		String body = "http://localhost:8080/checkemail/" + token;
		System.out.println(body);
		JMSUtli.sendEmail(createUser.getEmailId(), "verification email for user " + createUser.getFirstName(), body);
		return new ResponseDTO("User is ADDED" , userDTO);
//	}else {
//		throw new UserRegistrationException("User is already present", HttpStatus.OK, isUserPresent.get(), "false");
//	}
	}
	
	@Override
	public ResponseDTO getAllUser() {
		List<UserRegistrationModel> isUserPresent = userrepository.findAll();
		return new ResponseDTO("List of All Users", isUserPresent);
	}
	
	@Override
	public ResponseDTO getUserById(int id) throws InvalidDetailsException {
		Optional<UserRegistrationModel> isUserPresent = userrepository.findById(id);
		return new ResponseDTO("User of id:" +id, isUserPresent);
	}

	@Override
	public ResponseDTO updateUserById(String token,UserRegistrationDTO userDTO) {
		int Id = tokenutli.decodeToken(token);
		Optional<UserRegistrationModel> updateUser = userrepository.findById(Id);
		if(updateUser.isPresent()) {
		updateUser.get().setFirstName(userDTO.getFirstName());
		updateUser.get().setLastName(userDTO.getLastName());
		updateUser.get().setKyc(userDTO.getKyc());
		updateUser.get().setDateOfBirth(userDTO.getDateOfBirth());
		updateUser.get().setEmailId(userDTO.getEmailId());
		updateUser.get().setPassword(userDTO.getPassword());
		userrepository.save(updateUser.get());
		return new ResponseDTO("Update of user details is successfull", updateUser);
	}else {
		throw new UserRegistrationException(400, "User id is not present"); 
	}
	}
	
	@Override
	public ResponseDTO delete(String token, int id) {
		int Id = tokenutli.decodeToken(token);
		Optional<UserRegistrationModel> isUserPresent = userrepository.findById(Id);
		if(isUserPresent.isPresent()) {
			userrepository.delete(isUserPresent.get());
			return new ResponseDTO("User Data Deleted successfull",isUserPresent.get().getId());
		}else {
			throw new UserRegistrationException("User id is not present", HttpStatus.OK, isUserPresent.get(), "false");
		}
	}
	
	@Override
	public UserRegistrationModel check(String token) {
		int id = tokenutli.decodeToken(token);
		UserRegistrationModel verifyUser = userrepository.findById(id)
				.orElseThrow(() -> new UserRegistrationException(400, "User id is not present"));
		verifyUser.setVerify(true);
		userrepository.save(verifyUser);
		return verifyUser;
	}
	
	@Override
	public ResponseDTO loginUser(String token,String email,String password) {
	int Id = tokenutli.decodeToken(token);
	Optional<UserRegistrationModel> isUserPresent = userrepository.findById(Id);
	if(isUserPresent.isPresent()) {
		if(isUserPresent.get().getEmailId().equals(email)&&isUserPresent.get().getPassword().equals(password)) {
			String tokenForLogin = tokenutli.createToken(isUserPresent.get().getId());
			return new ResponseDTO("Login is successfull", "Token" + tokenForLogin);
		}else {
			throw new UserRegistrationException("Login failed", HttpStatus.OK, isUserPresent.get(), "false");
		} 
	}else {
			throw new UserRegistrationException("User id is not present", HttpStatus.OK, isUserPresent.get(), "false");
		}
	}

	@Override
	public ResponseDTO forgotPwd(String token,String email) {
		int Id = tokenutli.decodeToken(token);
		Optional<UserRegistrationModel> isUserPresent = userrepository.findById(Id);
		if(isUserPresent.isPresent()) {
			if(isUserPresent.get().getEmailId().equals(email)) {
				String body = "http://localhost:8080/resetpassword/" + tokenutli.createToken(isUserPresent.get().getId());
				JMSUtli.sendEmail(isUserPresent.get().getEmailId(), "Reset Password", body);
				return new ResponseDTO("Password is reset", body);
			}else {
				throw new UserRegistrationException("Email id is incorrect", HttpStatus.OK, null, "false");
			}
		}else {
			throw new UserRegistrationException("User id is not present", HttpStatus.OK, null, "false");
		}
		
	}
	
	@Override
	public ResponseDTO resetPassword(String token) {
		return new ResponseDTO("Password is resetted successfully", null);
	}
	
	@Override
	public Boolean verify(String token) {
		int Id = tokenutli.decodeToken(token);
		UserRegistrationModel verifyUser = userrepository.findById(Id)
				.orElseThrow(() -> new UserRegistrationException(400,"User id is not present" ));
		if(verifyUser != null) {
		return true;
		}else {
			return false;
		}
	}

	@Override
	public ResponseDTO sendotp(String token) {
		int Id = tokenutli.decodeToken(token);
		Optional<UserRegistrationModel> isUserPresent = userrepository.findById(Id);
		if(isUserPresent.isPresent()) {
			String body = "OTP = " +isUserPresent.get().getOtp();
			JMSUtli.sendEmail(isUserPresent.get().getEmailId(), "OTP(One Time Password)", body);
			return new ResponseDTO("OTP is Sent", body);
		}else {
			throw new UserRegistrationException("User id is not present", HttpStatus.OK, null, "false");
		}
		
	}

	@Override
	public ResponseDTO verifyotp(String token ,int otp) {
		int Id = tokenutli.decodeToken(token);
		Optional<UserRegistrationModel> isUserPresent = userrepository.findById(Id);
		if(isUserPresent.isPresent()) {
			if(isUserPresent.get().getOtp()==otp) {
				return new ResponseDTO("OTP is Verifed");
			}else {
				throw new UserRegistrationException("OTP is incorrect", HttpStatus.OK, isUserPresent.get(), "false");
		}
	}else {
			throw new UserRegistrationException("User id is not present", HttpStatus.OK, isUserPresent.get(), "false");
		}
	}

	
	
}
