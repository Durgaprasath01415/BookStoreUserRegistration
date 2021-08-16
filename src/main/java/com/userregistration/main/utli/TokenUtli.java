package com.userregistration.main.utli;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.Verification;

@Component
public class TokenUtli {
	public final String TOKEN_SECRET = "Durgaprasath";

	public String createToken(int l) {
		try {
			// to set algorithm
			Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);

			String token = JWT.create().withClaim("User_id", l).sign(algorithm);
			return token;
		} catch (JWTCreationException exception) {
			exception.printStackTrace();
			// log Token Signing Failed
		} catch (IllegalArgumentException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public int decodeToken(String token) {
		int userId;
		// for verification algorithm
		Verification verification = null;
		try {
			verification = JWT.require(Algorithm.HMAC256(TOKEN_SECRET));
		} catch (IllegalArgumentException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JWTVerifier jwtverifier = verification.build();
		// to decode token
		DecodedJWT decodedjwt = jwtverifier.verify(token);

		Claim claim = decodedjwt.getClaim("User_id");
		userId = claim.asInt();
		return userId;

	}
}
