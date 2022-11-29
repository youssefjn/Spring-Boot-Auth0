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
import org.springframework.web.bind.annotation.RestController;

import spring.boot.yj.entities.User;
import spring.boot.yj.exceptions.UserAlreadyExistsException;
import spring.boot.yj.requests.LoginBody;
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
		}
	}
	
	 @PostMapping("/login")
	public ResponseEntity<LoginResponse>loginUser(@Valid @RequestBody LoginBody loginBody){
		 String jwt = userService.loginUser(loginBody);
		 if(jwt == null) {
			 return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		 }else {
			 LoginResponse loginResponse= new LoginResponse();
			 loginResponse.setJwt(jwt);
			 return new ResponseEntity<LoginResponse>(loginResponse,HttpStatus.OK);
		 }
	 }
	 
	 @GetMapping("/me")
	public ResponseEntity<User> getLoggedInUserProfile(@AuthenticationPrincipal User user){
		 return new ResponseEntity<User> (user,HttpStatus.OK);
	 }
	
}
