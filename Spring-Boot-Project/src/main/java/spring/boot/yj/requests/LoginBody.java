package spring.boot.yj.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class LoginBody {
	@NotBlank
	@NotNull
	private String username;
	@NotBlank
	@NotNull
	private String password;
}
