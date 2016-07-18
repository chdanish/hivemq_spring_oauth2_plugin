/*
 * Copyright None
 */

package com.hivemq.plugin.spring_guice.security.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import com.hivemq.plugin.spring_guice.security.Oauthconfig;

/**
 * @author Danish
 */

@Configuration("OauthConfiguration")
@EnableOAuth2Client
public class OauthConfiguration implements Oauthconfig {

    //Additional instances of SystemMessage which are constructed in a different way
    //http://stackoverflow.com/questions/27864295/how-to-use-oauth2resttemplate
	
	    @Value("${oauth.resource:http://localhost:8080/uaa}")
	    private String baseUrl;
	    @Value("${oauth.authorize:http://localhost:8080/uaa/oauth/authorize}")
	    private String authorizeUrl;
	    //@Value("${oauth.token:http://localhost:8080/uaa/oauth/token}")
	    private String tokenUrl = "http://localhost:8080/uaa/oauth/token";


	    //@Bean(name = "resource")
	    protected OAuth2ProtectedResourceDetails resource(String username,String password) {

	        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();

	        ClientCredentialsResourceDetails resourcex = new ClientCredentialsResourceDetails();
	        
	        List <String> scopes = new ArrayList<String>(2);
	        scopes.add("openid");
	        scopes.add("read");
	        scopes.add("trust");
	        resource.setAccessTokenUri("http://localhost:8080/uaa/oauth/token");
	        resource.setClientId("acme");
	        resource.setClientSecret("acmesecret");
	        resource.setGrantType("password");
	        resource.setScope(scopes);

	        resource.setUsername(username);
	        resource.setPassword(password);
	        resource.setAuthenticationScheme(AuthenticationScheme.form);
	        //resource.setClientAuthenticationScheme(AuthenticationScheme.form);
	        return resource;
	    }

	   // @Bean(name = "OauthConfig")
	    @Override
	    public OAuth2RestOperations OauthConfig(String username,String password) {
	        AccessTokenRequest atr = new DefaultAccessTokenRequest();
	        return new OAuth2RestTemplate(resource(username,password), new DefaultOAuth2ClientContext(atr));
	    }

}
