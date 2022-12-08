package spring.boot.yj.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTDecodeException;

import spring.boot.yj.entities.User;
import spring.boot.yj.repositories.UserRepository;
import spring.boot.yj.service.JwtService;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{

	private JwtService jwtService;
	private UserRepository userRepository;

	public JwtRequestFilter(JwtService jwtService, UserRepository userRepository) {

		this.jwtService = jwtService;
		this.userRepository = userRepository;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String tokenHeader = request.getHeader("Authorization");
		if ( tokenHeader!=null && tokenHeader.startsWith("Bearer ")) {
			String token = tokenHeader.substring(7);
			try {
				String username = jwtService.getUsername(token);
				Optional<User>opUser = userRepository.findByUsernameIgnoreCase(username);
				if(opUser.isPresent()) {
					User user = opUser.get();
					if ( user.isEmailVerified()){
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null , new ArrayList<>());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);

					}
				}
			} catch (JWTDecodeException exception) {

			}

		}
		filterChain.doFilter(request, response);



	}



}
