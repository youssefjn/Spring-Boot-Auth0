package spring.boot.yj.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import spring.boot.yj.entities.User;
import spring.boot.yj.entities.VerificationToken;
import spring.boot.yj.exceptions.EmailFailureException;
import spring.boot.yj.exceptions.EmailNotFoundException;
import spring.boot.yj.exceptions.UserAlreadyExistsException;
import spring.boot.yj.exceptions.UserNotVerifiedException;
import spring.boot.yj.repositories.UserRepository;
import spring.boot.yj.repositories.VerificationTokenRepository;
import spring.boot.yj.requests.LoginBody;
import spring.boot.yj.requests.PasswordResetBody;
import spring.boot.yj.requests.RegistrationBody;

@Service
public class UserService {
	private UserRepository userRepository;
	private EncryptionService encryptionService;
	private JwtService jwtService;
	private EmailService emailService;
	private VerificationTokenRepository verificationTokenRepository;

	public UserService(UserRepository userRepository, EncryptionService encryptionService, JwtService jwtService,
			EmailService emailService, VerificationTokenRepository verificationTokenRepository) {

		this.userRepository = userRepository;
		this.encryptionService = encryptionService;
		this.jwtService = jwtService;
		this.emailService = emailService;
		this.verificationTokenRepository = verificationTokenRepository;
	}

	public User AddUser(RegistrationBody registrationBody) throws UserAlreadyExistsException, EmailFailureException {
		if (userRepository.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()
				|| userRepository.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()) {
			throw new UserAlreadyExistsException();
		}
		User user = new User();
		user.setFirstName(registrationBody.getFirstName());
		user.setLastName(registrationBody.getLastName());
		user.setEmail(registrationBody.getEmail());
		user.setUsername(registrationBody.getUsername());
		user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));
		VerificationToken verificationToken = createVerificationToken(user);
		emailService.sendVerificationEmail(verificationToken);
		return userRepository.save(user);

	}

	private VerificationToken createVerificationToken(User user) {
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(jwtService.generateVerificationJWT(user));
		verificationToken.setCreatedTimeStamp(new Timestamp(System.currentTimeMillis()));
		verificationToken.setUser(user);
		user.getVerificationTokens().add(verificationToken);
		return verificationToken;
	}

	public String loginUser(LoginBody loginBody) throws UserNotVerifiedException, EmailFailureException {
		Optional<User> opUser = userRepository.findByUsernameIgnoreCase(loginBody.getUsername());
		if (opUser.isPresent()) {
			User user = opUser.get();
			if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())) {
				if (user.isEmailVerified()) {

					return jwtService.generateJWT(user);
				} else {
					List<VerificationToken> verificationTokens = user.getVerificationTokens();
					boolean resend = verificationTokens.size() == 0 ||
							verificationTokens.get(0).getCreatedTimeStamp()
									.before(new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));
					if (resend) {
						VerificationToken verificationToken = createVerificationToken(user);
						verificationTokenRepository.save(verificationToken);
						emailService.sendVerificationEmail(verificationToken);
					}
					throw new UserNotVerifiedException(resend);
				}
			}
		}
		return null;

	}

	@Transactional
	public boolean verifyUser(String token) {
		Optional<VerificationToken> opToken = verificationTokenRepository.findByToken(token);
		if (opToken.isPresent()) {
			VerificationToken verificationToken = opToken.get();
			User user = verificationToken.getUser();
			if (!user.isEmailVerified()) {
				user.setEmailVerified(true);
				userRepository.save(user);
				verificationTokenRepository.deleteByUser(user);
				return true;
			}
		}
		return false;
	}

	public void forgotPassword(String email) throws EmailNotFoundException, EmailFailureException {
		Optional<User> opUser = userRepository.findByEmailIgnoreCase(email);
		if (opUser.isPresent()) {
			User user = opUser.get();
			String token = jwtService.generatePasswordResetJWT(user);
			emailService.sendPasswordResetEmail(user, token);
		} else {
			throw new EmailNotFoundException();
		}
	}

	public void resetPassword(PasswordResetBody passwordResetBody){
		String email = jwtService.getResetPasswordEmail(passwordResetBody.getToken());
		Optional<User> opUser = userRepository.findByEmailIgnoreCase(email);
		if(opUser.isPresent()){
			User user = opUser.get();
			user.setPassword(encryptionService.encryptPassword(passwordResetBody.getPassword()));
			userRepository.save(user);
		}
	}

}
