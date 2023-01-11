package spring.boot.yj.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
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
public class JwtRequestFilter extends OncePerRequestFilter implements ChannelInterceptor {

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
		UsernamePasswordAuthenticationToken token = checkToken(tokenHeader);
		if (token != null) {
			token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		}
		filterChain.doFilter(request, response);

	}

	private UsernamePasswordAuthenticationToken checkToken(String token) {
		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring(7);
			try {
				String username = jwtService.getUsername(token);
				Optional<User> opUser = userRepository.findByUsernameIgnoreCase(username);
				if (opUser.isPresent()) {
					User user = opUser.get();
					if (user.isEmailVerified()) {
						UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
								user, null, new ArrayList<>());
						SecurityContextHolder.getContext().setAuthentication(authenticationToken);
						return authenticationToken;
					}
				}
			} catch (JWTDecodeException exception) {}
		}
		SecurityContextHolder.getContext().setAuthentication(null);
		return null;
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		Map nativeHeaders = (Map) message.getHeaders().get("nativeHeaders");
		// TODO: Limit this to only CONNECT messages.
		if (nativeHeaders != null) {
			List authTokenList = (List) nativeHeaders.get("Authorization");
			if (authTokenList != null) {
				String tokenHeader = (String) authTokenList.get(0);
				checkToken(tokenHeader);
			}
		}
		return message;
	}

}
