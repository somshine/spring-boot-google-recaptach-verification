//package com.somshine.services;
//import java.net.URI;
//import java.util.regex.Pattern;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestOperations;
//
//import com.somshine.exceptions.ReCaptchaInvalidException;
//import com.somshine.exceptions.ReCaptchaUnavailableException;
//import com.somshine.models.GoogleResponse;
//
//
////Convert this in to service for the same use the Interface
//@Service("captchaServiceV3")
//public class CaptchaServiceV3 {
//	
//	@Autowired
//    protected HttpServletRequest request;
//	
//	@Autowired
//    protected RestOperations restTemplate;
//
//    private final static Logger LOGGER = LoggerFactory.getLogger(CaptchaServiceV3.class);
//    
//    public static final String REGISTER_ACTION = "register";
//    protected static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");
//    protected static final String RECAPTCHA_URL_TEMPLATE = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s";
//    
//    public void processResponse(String response, final String action) throws ReCaptchaInvalidException {
//        securityCheck(response);
//        
//        final URI verifyUri = URI.create(String.format(RECAPTCHA_URL_TEMPLATE, "6LesTTYdAAAAAKL6a-zPCrBJgMXWUX5DOtQmtyk4", response, getClientIP()));
//        try {
//            final GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);
//            LOGGER.debug("Google's response: {} ", googleResponse.toString());
//
//            if (!googleResponse.isSuccess() || !googleResponse.getAction().equals(action)) {
//                if (googleResponse.hasClientError()) {
//                }
//                throw new ReCaptchaInvalidException("reCaptcha was not successfully validated");
//            }
//        } catch (RestClientException rce) {
//            throw new ReCaptchaUnavailableException("Registration unavailable at this time.  Please try again later.", rce);
//        }
//    }
//    
//    protected String getClientIP() {
//        final String xfHeader = request.getHeader("X-Forwarded-For");
//        if (xfHeader == null) {
//            return request.getRemoteAddr();
//        }
//        return xfHeader.split(",")[0];
//    }
//    
//    protected void securityCheck(final String response) {
//        LOGGER.debug("Attempting to validate response {}", response);
//
//        if (!responseSanityCheck(response)) {
//            throw new ReCaptchaInvalidException("Response contains invalid characters");
//        }
//    }
//    
//    protected boolean responseSanityCheck(final String response) {
//        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
//    }
//}
