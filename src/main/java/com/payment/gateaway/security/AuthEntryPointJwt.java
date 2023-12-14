package com.payment.gateaway.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
    private String jwtSecret = "payment";
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String jwt = request.getHeader("auth-tgh");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        final Map<String, Object> body = new HashMap<>();
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
        } catch (MalformedJwtException e) {
            body.put("description","Not enough segments");
            body.put("title","401 Unauthorized");
        } catch (ExpiredJwtException e) {
            body.put("description","Signature has expired");
            body.put("title","401 Unauthorized");
        } catch (UnsupportedJwtException e) {
            body.put("description","JWT token is unsupported");
            body.put("title","401 Unauthorized");
        } catch (IllegalArgumentException e) {
            body.put("description","Missing Authorization Header");
            body.put("title","401 Unauthorized");
        }
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }



}
