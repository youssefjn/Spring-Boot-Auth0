package spring.boot.yj.service;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import spring.boot.yj.entities.User;

@Service
public class JwtService {

	@Value ("${jwt.algorithm.key}")
	private String algorithmKey;
	@Value ("${jwt.issuer}")
	private String Issuer;
	@Value("${jwt.expiryInSeconds}")
	private int expiryInSeconds;
	private Algorithm algorithm;
	private static final String USERNAME_KEY="USERNAME";
	private static final String VERIFICATION_EMAIL_KEY="VERIFICATION_EMAIL";
	private static final String RESET_PASSWORD_KEY="RESET_PASSWORD_KEY";
	@PostConstruct
	public void postconstruct() {
		algorithm= Algorithm.HMAC256(algorithmKey);
	}
	public String generateJWT(User user) {
		return JWT.create()
				.withClaim(USERNAME_KEY, user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+(1000*expiryInSeconds)))
				.withIssuer(Issuer).sign(algorithm);
	}

	public String generateVerificationJWT(User user) {
		return JWT.create()
				.withClaim(VERIFICATION_EMAIL_KEY, user.getEmail())
				.withExpiresAt(new Date(System.currentTimeMillis()+(1000 * 60 * 30)))
				.withIssuer(Issuer).sign(algorithm);
	}

	public String generatePasswordResetJWT(User user) {
		return JWT.create()
				.withClaim(RESET_PASSWORD_KEY, user.getEmail())
				.withExpiresAt(new Date(System.currentTimeMillis()+(1000*expiryInSeconds)))
				.withIssuer(Issuer).sign(algorithm);
	}

	public String getResetPasswordEmail ( String token){
		DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
		return jwt.getClaim(RESET_PASSWORD_KEY).asString();
	}

	public String getUsername(String token) {
		DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
		return jwt.getClaim(USERNAME_KEY).asString();
	}


}
