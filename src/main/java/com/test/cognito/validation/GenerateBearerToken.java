package com.test.cognito.validation;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthResponse;

@Component
public class GenerateBearerToken {
	
	@Value("${cognito.clientId}")
    private String clientId;
	@Value("${cognito.userpool}")
    private String userpool;
	private final String authFlow = "USER_PASSWORD_AUTH";
	private Logger logger = LogManager.getLogger(GenerateBearerToken.class);	
	
	public String getToken(String user, String pass) {     
      Map<String,String> authParams = new HashMap<>();
      authParams.put("USERNAME",user);
      authParams.put("PASSWORD",pass);
        CognitoIdentityProviderClient identityProviderClient = CognitoIdentityProviderClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();        
        logger.info("Bearer Token generation inititated");
        String bearerToken = InitiateAuth(identityProviderClient, authFlow, clientId, authParams);
        identityProviderClient.close();
        return bearerToken;
	}

	protected String InitiateAuth(CognitoIdentityProviderClient identityProviderClient, String authFlow, String clientId, Map<String,String> authParams){
        InitiateAuthRequest req = InitiateAuthRequest.builder()
                .authFlow(authFlow)
                .clientId(clientId)
                .authParameters(authParams)
                .build();
        InitiateAuthResponse resp =identityProviderClient.initiateAuth(req);
        logger.info("Bearer Token generated");
        return resp.authenticationResult().idToken();
    }
	
}
