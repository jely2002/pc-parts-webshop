package com.jelleglebbeek.pcparts.auth;

import com.jelleglebbeek.pcparts.user.UserPrincipalService;
import io.jsonwebtoken.ClaimJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;
    private final UserPrincipalService userPrincipalService;
    private final AuthService authService;
    private final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    @Value("${jwt.cookie-name}")
    private String cookieName;

    @Autowired
    public TokenAuthenticationFilter(
            JwtHelper jwtHelper,
            UserPrincipalService userPrincipalService,
            AuthService authService
    ) {
        this.jwtHelper = jwtHelper;
        this.userPrincipalService = userPrincipalService;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtToken(httpServletRequest);
            if (StringUtils.hasText(jwt)) {
                Jwt decodedToken;
                try {
                    decodedToken = jwtHelper.decode(jwt);
                } catch (ClaimJwtException e){
                   HttpCookie httpCookie = authService.createCookie("", 0);
                   Cookie cookie = new Cookie(httpCookie.getName(), httpCookie.getValue());
                   httpServletResponse.addCookie(cookie);
                   httpServletResponse.setStatus(401);
                   filterChain.doFilter(httpServletRequest, httpServletResponse);
                   return;
                }
                String username = decodedToken.getSubject();
                UserDetails userDetails = userPrincipalService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String getJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private String getJwtToken(HttpServletRequest request) {
        String tokenFromCookie = getJwtFromCookie(request);
        if (tokenFromCookie == null) {
            return getJwtFromRequest(request);
        } else {
            return tokenFromCookie;
        }
    }
}
