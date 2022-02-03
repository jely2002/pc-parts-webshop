package com.jelleglebbeek.pcparts.auth;

import com.jelleglebbeek.pcparts.auth.model.GoogleResponse;
import com.jelleglebbeek.pcparts.exceptions.InvalidRecaptchaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.regex.Pattern;

@Service
public class CaptchaService {

    private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    private final RestOperations restTemplate = new RestTemplate();

    @Value("${captcha.secret}")
    private String secret;

    public void verifyCaptcha(String response, String ip) {
        if(!responseSanityCheck(response)) {
            throw new InvalidRecaptchaException("Response contains invalid characters");
        }

        URI verifyUri = URI.create(String.format(
                "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s",
                secret, response, ip));

        GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);

        if(googleResponse == null || !googleResponse.isSuccess()) {
            throw new InvalidRecaptchaException("reCaptcha was not successfully validated");
        }
    }

    private boolean responseSanityCheck(String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }
}
