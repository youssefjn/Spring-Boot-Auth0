package spring.boot.yj.security;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authorization.AuthorizationEventPublisher;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.SpringAuthorizationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.messaging.access.intercept.AuthorizationChannelInterceptor;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import spring.boot.yj.entities.User;
import spring.boot.yj.service.UserService;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private ApplicationContext context;
    private JwtRequestFilter filter;
    private UserService userService;
    private static final AntPathMatcher MATCHER = new AntPathMatcher();
    public WebSocketConfiguration(ApplicationContext context, JwtRequestFilter filter, UserService userService) {
        this.context = context;
        this.filter = filter;
        this.userService = userService;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket").setAllowedOriginPatterns("**").withSockJS();

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    private AuthorizationManager<Message<?>> makeMessageAuthorizationManager() {
        MessageMatcherDelegatingAuthorizationManager.Builder messages = new MessageMatcherDelegatingAuthorizationManager.Builder();
        messages.simpDestMatchers("/topic/user/**").authenticated()
                .anyMessage().permitAll();
        return messages.build();
    }

    

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        AuthorizationManager<Message<?>> authorizationManager = 
            makeMessageAuthorizationManager();
        AuthorizationChannelInterceptor authInterceptor = 
            new AuthorizationChannelInterceptor(authorizationManager);
        AuthorizationEventPublisher publisher = 
            new SpringAuthorizationEventPublisher(context);
        authInterceptor.setAuthorizationEventPublisher(publisher);
        registration.interceptors(filter, authInterceptor, 
        new RejectClientMessagesOnChannelsChannelInterceptor(), 
        new DestinationLevelAuthorizationChannelInterceptor());

    }

    private class RejectClientMessagesOnChannelsChannelInterceptor implements ChannelInterceptor{

        private String[] paths =new String[]{
            "/topic/user/*/address"
        };

        @Override
        public Message<?> preSend(Message<?> message, MessageChannel channel) {
            
            if (message.getHeaders().get("simpMessagetype").equals(SimpMessageType.MESSAGE)) {
                String destination = (String) message.getHeaders().get("simpDestination");
                for (String path:paths) {
                    if (MATCHER.match(path, destination)) {
                        message = null;
                    }
                }
            }
                return message;
        }

        
    }

    private class DestinationLevelAuthorizationChannelInterceptor implements ChannelInterceptor{

        @Override
        public Message<?> preSend(Message<?> message, MessageChannel channel) {
            if (message.getHeaders().get("simpMessagetype").equals(SimpMessageType.SUBSCRIBE)) {
                String destination = (String) message.getHeaders().get("simpDestination");
                Map<String,String> params = MATCHER.extractUriTemplateVariables("/topic/user/{userId}/**", destination);
                try {
                    Long userId =Long.valueOf(params.get("userId"));
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    if (authentication != null) {
                        User user = (User) authentication.getPrincipal();
                        if (!userService.userHasPermissionToUser(user, userId)) {
                            message = null;

                        }
                    } else {
                        message = null;
                    }
                } catch (NumberFormatException ex) {
                    message = null;
                }
                
            }
            return message;
        }

        

    }
}
