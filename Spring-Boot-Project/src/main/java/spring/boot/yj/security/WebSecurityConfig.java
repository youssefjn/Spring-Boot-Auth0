package spring.boot.yj.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class WebSecurityConfig {
private JwtRequestFilter jwtRequestFilter;

	
	public WebSecurityConfig(JwtRequestFilter jwtRequestFilter) {

	this.jwtRequestFilter = jwtRequestFilter;
}


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	
		http.csrf().disable().cors().disable();
		http.addFilterBefore(jwtRequestFilter,AuthorizationFilter.class);
		http.authorizeHttpRequests()
		.antMatchers("/product","/auth/register",
		"/auth/login", "/auth/verify", "auth/forgot", "auth/reset", "/websocket","/websocket/**").permitAll()
		.anyRequest().authenticated();
		return http.build();
	}
	
	
}
