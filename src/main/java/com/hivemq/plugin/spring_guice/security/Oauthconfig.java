package com.hivemq.plugin.spring_guice.security;

import org.springframework.security.oauth2.client.OAuth2RestOperations;

public interface Oauthconfig {

	

	OAuth2RestOperations OauthConfig(String username, String password);

}
