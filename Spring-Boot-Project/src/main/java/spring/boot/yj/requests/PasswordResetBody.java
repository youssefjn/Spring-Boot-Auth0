package spring.boot.yj.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class PasswordResetBody {
    @NotBlank
    @NotNull
    private String token;
    @NotBlank
	@NotNull
	@Size (min = 6)
    private String password;
}
