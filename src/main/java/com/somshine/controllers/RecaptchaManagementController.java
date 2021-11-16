package com.somshine.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.somshine.models.*;

@RestController
public class RecaptchaManagementController {

	public static final String url = "https://www.google.com/recaptcha/api/siteverify";
	public static final String secret = "";

	@RequestMapping(value = "/verifyUser.do", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public boolean viewAllItems(@RequestBody String gRecaptchaResponse) {
		System.out.println(gRecaptchaResponse);
		if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
			return false;
		}
		try {

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(url).queryParam("secret", secret)
					.queryParam("response", gRecaptchaResponse);
			HttpEntity<String> entity = new HttpEntity<>(headers);

			ResponseEntity<Response> response = restTemplate.exchange(builder
					.build().encode().toUri(), HttpMethod.GET, entity,
					Response.class);

			Response rs = response.getBody();
			System.out.println(rs.isSuccess());

			if (response.getStatusCode().equals("200")) {
				return rs.isSuccess();
			}

			return false;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
}
