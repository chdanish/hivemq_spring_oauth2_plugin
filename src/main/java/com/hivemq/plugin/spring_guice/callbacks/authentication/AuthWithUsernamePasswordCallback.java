package com.hivemq.plugin.spring_guice.callbacks.authentication;

import com.hivemq.plugin.spring_guice.security.Oauthconfig;
import com.hivemq.plugin.spring_guice.security.Impl.OauthConfiguration;
import com.hivemq.spi.callback.CallbackPriority;
import com.hivemq.spi.callback.exception.AuthenticationException;
import com.hivemq.spi.callback.security.OnAuthenticationCallback;
import com.hivemq.spi.message.ReturnCode;
import com.hivemq.spi.security.ClientCredentialsData;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author Florian Limpoeck
 */
@Component
public class AuthWithUsernamePasswordCallback implements OnAuthenticationCallback {

    private static Logger log = LoggerFactory.getLogger(AuthWithUsernamePasswordCallback.class);

    /**
     * This is an example authentication method, which shows all possible data that is available from the client.
     * The authentication should be replace with your method of choice: database lookup, webservice call, ...
     *
     * @param clientCredentialsData All information from the connecting MQTT client
     * @return true, if the client provides correct username and password, false if credentials are not present or wrong
     * @throws AuthenticationException if the client is not authorized and it should be disconnected immediately
     */
    
    @Inject
    ApplicationContext ctx;
    
    // This is an example to use spring configuration in-case @Autowired  is not available.
    private OauthConfiguration getOauthConfiguration(){
    	return (OauthConfiguration)((AnnotationConfigApplicationContext) ctx).getBeanFactory().getBean("OauthConfiguration");
    }

    //This is an example to use guice's @Inject in-case spring configuration is not available.
    @Inject
    private Oauthconfig oauthConfiguration;
    
    

    @Override
    public Boolean checkCredentials(ClientCredentialsData clientCredentialsData) throws AuthenticationException {

        String clientId = clientCredentialsData.getClientId();

        log.info("A new client with id {} requests authentication from the AuthWithUsernamePasswordCallback", clientId);

        if (clientCredentialsData.getUsername().isPresent() && clientCredentialsData.getPassword().isPresent()) {
            String username = clientCredentialsData.getUsername().get();
            String password = clientCredentialsData.getPassword().get();

            log.info("The client provides the following username: {}", username);

            if (isUserValid(username, password)) {
                return true;
            } else {
                throw new AuthenticationException(ReturnCode.REFUSED_NOT_AUTHORIZED);
            }
        }

        return false;
    }

    private boolean isUserValid(String username, String password) {

        // Here goes your custom authentication logic !!!
        // This is just a dummy implementation returning true at dummy/password !!

        // You want to validate username and password against your database or third party service

    	OAuth2AccessToken mytoken;
    	
        if(StringUtils.hasText(username) && StringUtils.hasText(password)){
        	try {
        		mytoken = getOauthConfiguration().OauthConfig(username, password).getAccessToken();
        		if(mytoken != null){
        			log.info("toString: {}", mytoken.toString());
        			return true;
        		}
			} catch (Exception e) {
				return false;
			}
        	
            return true;
        }

        else {
            return false;
        }
    }

    /**
     * The priority is used when more than one OnAuthenticationCallback is implemented to determine the order.
     * If there is only one callback, which implements a certain interface, the priority has no effect.
     *
     * @return callback priority
     */
    @Override
    public int priority() {
        return CallbackPriority.CRITICAL;
    }
}