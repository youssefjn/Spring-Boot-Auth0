package spring.boot.yj.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class RegistrationBody {
	@NotBlank
	@NotNull
	private String firstName;
	@NotBlank
	@NotNull
	private String lastName;
	@NotBlank
	@NotNull
	@Size (min = 6)
	private String password;
	@NotBlank
	@NotNull
	@Email
	private String email;
	@NotBlank
	@NotNull
	@Size (min = 6)
	private String username;
}
