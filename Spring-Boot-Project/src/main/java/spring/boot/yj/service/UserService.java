package spring.boot.yj.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spring.boot.yj.entities.User;
import spring.boot.yj.exceptions.UserAlreadyExistsException;
import spring.boot.yj.repositories.UserRepository;
import spring.boot.yj.requests.LoginBody;
import spring.boot.yj.requests.RegistrationBody;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
private EncryptionService encryptionService;
private JwtService jwtService;
	public UserService(UserRepository userRepository, EncryptionService encryptionService,JwtService jwtService) {

		this.userRepository = userRepository;
		this.encryptionService = encryptionService;
		this.jwtService= jwtService;
	}

	public User AddUser(RegistrationBody registrationBody) throws UserAlreadyExistsException{
		if(userRepository.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent() 
				|| userRepository.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()) {
			throw new UserAlreadyExistsException(); 
		}
		User user = new User();
		user.setFirstName(registrationBody.getFirstName());
		user.setLastName(registrationBody.getLastName());
		user.setEmail(registrationBody.getEmail());
		user.setUsername(registrationBody.getUsername());
		user.setPassword(encryptionService.encryptPassword( registrationBody.getPassword()));
		return userRepository.save(user);

	}
	public String loginUser(LoginBody loginBody) {
		Optional<User>opUser = userRepository.findByUsernameIgnoreCase(loginBody.getUsername());
		if ( opUser.isPresent()) {
			User user = opUser.get();
			if (encryptionService.verifyPassword(loginBody.getPassword(),user.getPassword())) {
				return jwtService.generateJWT(user);
			}
		}
		return null;
		
	}
	
}
