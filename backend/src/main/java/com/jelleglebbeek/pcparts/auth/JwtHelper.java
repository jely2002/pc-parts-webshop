package com.jelleglebbeek.pcparts.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
public class JwtHelper {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Long lifetime;

    public String createJwtForClaims(String subject, Map<String, Object> claims) {
        Calendar calendar = Calendar.getInstance();
        long millis = Instant.now().toEpochMilli() + lifetime;
        calendar.setTimeInMillis(millis);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setExpiration(calendar.getTime())
                .setIssuedAt(new Date())
                .setIssuer("bouncer-api")
                .setAudience("bouncer-client")
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Jwt decode(String token) {
        io.jsonwebtoken.Jws<Claims> jwt = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);

        Date issuedAt = jwt.getBody().getIssuedAt();
        Date expiration = jwt.getBody().getExpiration();
        JwsHeader<?> headers = jwt.getHeader();
        Claims claims = jwt.getBody();

        return new Jwt("bouncer-api", issuedAt.toInstant(), expiration.toInstant(), headers, claims);
    }

}
