package com.test.cognito.validation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTValidator {

	@Value("${cognito.clientId}")
    private String clientId;
	@Value("${cognito.userpool}")
    private String userpool;
	
	private Logger logger = LogManager.getLogger(JWTValidator.class);
	
	public JwtClaims validateAccess(String token)  {		
		JwtConsumer jwtConsumer = new JwtConsumerBuilder()
				.setRequireExpirationTime()
				.setAllowedClockSkewInSeconds(60)
				.setRequireSubject()
				.setExpectedAudience(clientId)
				.setExpectedIssuer("https://cognito-idp."+userpool.split("_")[0]+".amazonaws.com/"+userpool)
	            .setVerificationKeyResolver(new HttpsJwksVerificationKeyResolver(new HttpsJwks("https://cognito-idp."+userpool.split("_")[0]+".amazonaws.com/"+userpool+"/.well-known/jwks.json")))
	            .setJwsAlgorithmConstraints(ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256) 
	            .build();		
		try
	    {
	        JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
	        logger.info("JWT validation succeeded! " + jwtClaims);       
	        return jwtClaims;
	    }
	    catch (InvalidJwtException e){
	    	logger.info("Invalid JWT! " + e);
	    	return null;
	    }		
	}	
}
