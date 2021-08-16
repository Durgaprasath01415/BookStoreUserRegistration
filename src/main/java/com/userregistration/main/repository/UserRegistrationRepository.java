package com.userregistration.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.userregistration.main.model.UserRegistrationModel;

@Repository
public interface UserRegistrationRepository extends JpaRepository<UserRegistrationModel, Integer> {

	Optional<UserRegistrationModel> findByotp(double otp);

	Optional<UserRegistrationModel> findByEmailId(String email);

}
