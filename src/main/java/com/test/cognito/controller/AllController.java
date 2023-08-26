package com.test.cognito.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jose4j.jwt.JwtClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.cognito.User.UserResponse;
import com.test.cognito.admin.AdminResponse;
import com.test.cognito.error.ErrorResponse;
import com.test.cognito.login.LoginRequest;
import com.test.cognito.login.LoginResponse;
import com.test.cognito.validation.GenerateBearerToken;
import com.test.cognito.validation.JWTValidator;

import software.amazon.awssdk.awscore.exception.AwsServiceException;

@RestController
@RequestMapping("/")
public class AllController {

	private Logger logger = LogManager.getLogger(AllController.class);
	
	@Autowired
	private GenerateBearerToken bearerToken;
	@Autowired
	private JWTValidator jwtValidator;
	
	@PostMapping("/loginPage")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
		logger.info("loginRequest : "+loginRequest.toString());			
		String JWT = "";		
		try {
			JWT = bearerToken.getToken(loginRequest.getUsername(), loginRequest.getPassword());
		}
		catch(AwsServiceException e) {
			e.printStackTrace();			
			ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), "Invalid Username/Password");
			return new ResponseEntity<ErrorResponse>(errorResponse,HttpStatus.OK);
		}		
		LoginResponse loginResponse = new LoginResponse(JWT);		
		return new ResponseEntity<LoginResponse>(loginResponse,HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/adminPage")
	public ResponseEntity<?> adminLogin(@RequestHeader("Authorization") String Token){
		JwtClaims jwtClaims = jwtValidator.validateAccess(Token.split(" ")[1]);
		if( jwtClaims != null ) {
			if(((List<String>)jwtClaims.getClaimValue("cognito:groups")).contains("admin")) {
				logger.info("Allowed ADMIN access");
				AdminResponse adminResponse = new AdminResponse();
				return new ResponseEntity<AdminResponse>(adminResponse, HttpStatus.OK);
			}
		}	
		logger.info("Not allowed admin access");
		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),"Not allowed to access this page");
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.UNAUTHORIZED);
	}
	
	@GetMapping("/userPage")
	public ResponseEntity<?> userLogin(@RequestHeader("Authorization") String Token){
		JwtClaims jwtClaims = jwtValidator.validateAccess(Token.split(" ")[1]);
		if( jwtClaims != null ) {
			logger.info("Allowed USER access");
			UserResponse userResponse = new UserResponse();
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);			
		}	
		logger.info("Not allowed user access");
		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),"Not allowed to access this page");
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.UNAUTHORIZED);
	}
}
