package com.somshine.controllers;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.somshine.models.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class RecaptchaManagementController {
	public static final String url = "https://www.google.com/recaptcha/api/siteverify";
	protected static final String RECAPTCHA_URL_TEMPLATE = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s";
	public static final String secret = "6LesTTYdAAAAAKL6a-zPCrBJgMXWUX5DOtQmtyk4";

	private final static Logger LOGGER = LoggerFactory.getLogger(RecaptchaManagementController.class);

	@Autowired
	protected HttpServletRequest request;

	@RequestMapping(value = "/verifyUser.do", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public Response viewAllItems(@RequestBody ScoreRequest objScoreRequest) {
		System.out.println(objScoreRequest.toString());
		
		Response objResponse = new Response();
		objResponse.setSuccess(false);
		objResponse.setMessage("Json data with response key:value not available in request.");

		if (objScoreRequest.getResponse() == null || "".equals(objScoreRequest.getResponse())) {
			objResponse.setSection("Captcha Data Failed");
			return objResponse;
		}
		
		try {
			RestTemplate restTemplate = new RestTemplate();
			final URI verifyUri = URI.create(String.format(RECAPTCHA_URL_TEMPLATE, "6LesTTYdAAAAAKL6a-zPCrBJgMXWUX5DOtQmtyk4", objScoreRequest.getResponse(), getClientIP()));
			final GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);
			LOGGER.debug("Google's response: {} ", googleResponse.toString());

			if (!googleResponse.isSuccess()) {
				System.err.println("Failed to validate the token");
			}

			if (googleResponse.isSuccess()) {
				objResponse.setSuccess(googleResponse.isSuccess());
				objResponse.setMessage("Token validated successfully.");
				return objResponse;
			} else {
				objResponse.setSection("Response");
				objResponse.setMessage(googleResponse.toString());
			}

			return objResponse;

		} catch (Exception e) {
			e.printStackTrace();
			objResponse.setSection("Exception");
			objResponse.setMessage("Exception: " + e.getMessage());
			return objResponse;
		}

	}

	protected String getClientIP() {
		final String xfHeader = request.getHeader("X-Forwarded-For");
		if (xfHeader == null) {
			return request.getRemoteAddr();
		}
		return xfHeader.split(",")[0];
	}
}
