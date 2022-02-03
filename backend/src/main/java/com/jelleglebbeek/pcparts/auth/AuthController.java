package com.jelleglebbeek.pcparts.auth;

import com.jelleglebbeek.pcparts.auth.model.UserInitializeLogin;
import com.jelleglebbeek.pcparts.auth.model.UserInitializeLoginResult;
import com.jelleglebbeek.pcparts.auth.model.UserLogin;
import com.jelleglebbeek.pcparts.auth.model.UserLoginResult;
import com.jelleglebbeek.pcparts.user.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final CaptchaService captchaService;

    @Value("${jwt.lifetime}")
    private Long lifetime;

    private static final int millisecondsToSeconds = 1000;

    @Autowired
    public AuthController(AuthService authService, CaptchaService captchaService) {
        this.authService = authService;
        this.captchaService = captchaService;
    }

    @PostMapping("/captcha")
    public void verifyCaptcha(@RequestBody String token, HttpServletRequest request) {
        this.captchaService.verifyCaptcha(token, request.getRemoteAddr());
    }

    @PostMapping("/login/initialize")
    public UserInitializeLoginResult loginInitializeUser(@RequestBody UserInitializeLogin userInitializeLogin) {
        return authService.loginInitializeUser(userInitializeLogin);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody UserLogin userLogin) throws GeneralSecurityException {
        UserLoginResult result = authService.loginUser(userLogin);
        HttpCookie cookie =  authService.createCookie(result.getToken(), lifetime / millisecondsToSeconds);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(result.getUser());
    }

    @PostMapping("/logout")
    public ResponseEntity<UserLoginResult> logoutUser() {
        HttpCookie cookie = authService.createCookie("", 0);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(null);
    }

    @GetMapping("/user")
    public User getUser() {
        return authService.getUser();
    }

}
