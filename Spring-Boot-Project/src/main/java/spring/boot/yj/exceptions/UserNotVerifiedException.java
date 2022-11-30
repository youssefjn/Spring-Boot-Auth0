package spring.boot.yj.exceptions;

import lombok.Getter;

@Getter
public class UserNotVerifiedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean newEmailSent;

	public UserNotVerifiedException(boolean newEmailSent) {
		this.newEmailSent = newEmailSent;
		
	}

	
}
