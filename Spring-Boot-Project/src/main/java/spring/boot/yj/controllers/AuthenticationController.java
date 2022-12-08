package spring.boot.yj.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import spring.boot.yj.entities.User;
import spring.boot.yj.exceptions.EmailFailureException;
import spring.boot.yj.exceptions.EmailNotFoundException;
import spring.boot.yj.exceptions.UserAlreadyExistsException;
import spring.boot.yj.exceptions.UserNotVerifiedException;
import spring.boot.yj.requests.LoginBody;
import spring.boot.yj.requests.PasswordResetBody;
import spring.boot.yj.requests.RegistrationBody;
import spring.boot.yj.response.LoginResponse;
import spring.boot.yj.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	@Autowired
	private UserService userService;

	public AuthenticationController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping ( "/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationBody registrationBody) {
		try {
			userService.AddUser(registrationBody);
			return new ResponseEntity<RegistrationBody>(registrationBody,HttpStatus.CREATED);
		} catch (UserAlreadyExistsException e) {
			return new ResponseEntity<String>("User exists",HttpStatus.CONFLICT);
		} catch (EmailFailureException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse>loginUser(@Valid @RequestBody LoginBody loginBody){
		String jwt;
		try {
			jwt = userService.loginUser(loginBody);
		} catch (UserNotVerifiedException e) {
			LoginResponse loginResponse = new LoginResponse();
			loginResponse.setSuccess(false);
			String reason ="USER_NOT_VERIFIED";
			if ( e.isNewEmailSent()) {
				reason+="_EMAIL_RESENT";
			}
			loginResponse.setFailureReason(reason);
			return new ResponseEntity<>(loginResponse, HttpStatus.FORBIDDEN);
		} catch (EmailFailureException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(jwt == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}else {
			LoginResponse loginResponse= new LoginResponse();
			loginResponse.setJwt(jwt);
			loginResponse.setSuccess(true);

			return new ResponseEntity<LoginResponse>(loginResponse,HttpStatus.OK);
		}
	}

	@GetMapping("/me")
	public ResponseEntity<User> getLoggedInUserProfile(@AuthenticationPrincipal User user){
		return new ResponseEntity<User> (user,HttpStatus.OK);
	}

	@PostMapping("/verify")
	public ResponseEntity<?>verifyEmail(@RequestParam String token){
		if(userService.verifyUser(token)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/forgot")
	public ResponseEntity<?> forgotPassword(@RequestParam String email ){
		try {
			userService.forgotPassword(email);
			return new ResponseEntity<> (HttpStatus.OK);
		} catch (EmailNotFoundException e) {
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (EmailFailureException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/reset")
	public ResponseEntity<?> resetPassword (@Valid @RequestBody PasswordResetBody  passwordResetBody){
		userService.resetPassword(passwordResetBody);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
