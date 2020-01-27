package com.fernbird.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fernbird.model.OTPDTO;
import com.fernbird.model.OTPModel;
import com.messagebird.MessageBirdClient;
import com.messagebird.MessageBirdService;
import com.messagebird.MessageBirdServiceImpl;
import com.messagebird.exceptions.GeneralException;
import com.messagebird.exceptions.NotFoundException;
import com.messagebird.exceptions.UnauthorizedException;
import com.messagebird.objects.Verify;
import com.messagebird.objects.VerifyRequest;
import com.messagebird.objects.VerifyType;

@RestController
public class OTPController {
	 // Create a MessageBirdService
     final MessageBirdService messagebirdservice = new MessageBirdServiceImpl(("rfqLiIEfCISZGCx2rB7GGPIfJ"));
	 
	 //ADD THE SERVICE CLIENTS
	 final MessageBirdClient messagebirdclient = new MessageBirdClient(messagebirdservice);
	 
	 @RequestMapping(value = "/sendOTP", method = RequestMethod.POST)
		ResponseEntity<Object> sendOTP(@RequestBody OTPDTO number) {
		 
			Map<String, Object> model = new HashMap<>();
			try {
				VerifyRequest verifyRequest = new VerifyRequest(number.getNumber());
				verifyRequest.setTimeout(1200);

				final Verify verify = messagebirdclient.sendVerifyToken(verifyRequest);
				model.put("otpId", verify.getId());
				System.out.println(model);
				//return new ResponseEntity<Object>(new MessageResponse("User registered successfully!"));
				return new ResponseEntity<>("otp send sucessfully",HttpStatus.ACCEPTED);
				
			} catch (UnauthorizedException | GeneralException ex) {
				model.put("errors", ex.toString());
				return new ResponseEntity<>("OTP is notsend ", HttpStatus.BAD_REQUEST);
			}
		}
		
			 
	 
	@PostMapping({ "/verify" })
	ResponseEntity<Object> verificationotp(@RequestBody OTPDTO otp) throws NotFoundException {
		
		String id = otp.getId();
		String token = otp.getToken();

		System.out.println("id"+ id);
		System.out.println("token"+ token);
		
		Map<String, Object> model = new HashMap<>();
		try {
			
			final Verify verify = messagebirdclient.verifyToken(id,token);
			
			
			return new ResponseEntity<>("OTP verified", HttpStatus.OK);
			
			
		} catch (UnauthorizedException | GeneralException ex) {
			
			model.put("errors", ex.toString());
			return new ResponseEntity<>("OTP cannot verified", HttpStatus.BAD_REQUEST);
			
		}
		

	}
}
