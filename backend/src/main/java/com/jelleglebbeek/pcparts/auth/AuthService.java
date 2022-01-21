package com.jelleglebbeek.pcparts.auth;

import com.jelleglebbeek.pcparts.auth.model.UserInitializeLogin;
import com.jelleglebbeek.pcparts.auth.model.UserInitializeLoginResult;
import com.jelleglebbeek.pcparts.auth.model.UserLogin;
import com.jelleglebbeek.pcparts.auth.model.UserLoginResult;
import com.jelleglebbeek.pcparts.exceptions.WrongCredentialsException;
import com.jelleglebbeek.pcparts.user.UserPrincipalService;
import com.jelleglebbeek.pcparts.user.UserService;
import com.jelleglebbeek.pcparts.user.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Value("${jwt.cookie-name}")
    private String cookieName;

    @Value("${jwt.cookie-secure}")
    private Boolean secureCookie;

    @Value("${jwt.cookie.restrict-site}")
    private Boolean sameSiteStrict;

    private final UserPrincipalService userPrincipalService;
    private final Argon2PasswordEncoder argon2PasswordEncoder;
    private final JwtHelper jwtHelper;
    private final UserService userService;

    @Autowired
    public AuthService(
            UserPrincipalService userPrincipalService,
            Argon2PasswordEncoder argon2PasswordEncoder,
            JwtHelper jwtHelper,
            UserService userService
    ) {
        this.userPrincipalService = userPrincipalService;
        this.argon2PasswordEncoder = argon2PasswordEncoder;
        this.jwtHelper = jwtHelper;
        this.userService = userService;
    }

    public UserInitializeLoginResult loginInitializeUser(UserInitializeLogin userInitializeLogin) {
        User user = this.userService.findOneByEmail(userInitializeLogin.getEmail());
        return new UserInitializeLoginResult(user.getPublicKey());
    }

    public UserLoginResult loginUser(UserLogin userLogin) throws GeneralSecurityException {
        UserDetails userDetails;
        try {
            userDetails = userPrincipalService.loadUserByUsername(userLogin.getEmail());
        } catch (UsernameNotFoundException e) {
            throw new WrongCredentialsException();
        }

        if (!argon2PasswordEncoder.matches(decryptPassword(userLogin), userDetails.getPassword())) {
            throw new WrongCredentialsException();
        }

        Map<String, Object> claims = new HashMap<>();
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        claims.put(HttpSecurityConfig.AUTHORITIES_CLAIM_NAME, authorities);

        String jwt = jwtHelper.createJwtForClaims(userLogin.getEmail(), claims);

        User user = this.userService.findOneByEmail(userDetails.getUsername());
        return new UserLoginResult(jwt, user);
    }

    public User getUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userService.findOneByEmail(username);
    }

    public HttpCookie createCookie(String value, long maxAge) {
        return ResponseCookie.from(cookieName, value)
                .path("/")
                .httpOnly(true)
                .maxAge(maxAge)
                .secure(secureCookie)
                .sameSite(sameSiteStrict ? "Strict" : "Lax")
                .build();
    }

    private String decryptPassword(UserLogin userLogin) throws GeneralSecurityException {
        User user = this.userService.findOneByEmail(userLogin.getEmail());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Cipher decryptCipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(user.getPrivateKey())));
        OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT);
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey, oaepParams);
        byte[] decrypted = decryptCipher.doFinal(Base64.getDecoder().decode(userLogin.getPassword()));
        return new String(decrypted, StandardCharsets.UTF_8);
    }

}
