package com.payment.gateaway.security;

import com.payment.gateaway.service.UserDetailService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailService adminDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = adminDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }  catch (ExpiredJwtException e) {
            throw new RuntimeException("Token has expired");
        }catch (MalformedJwtException e) {
            throw new RuntimeException("Invalid JWT token: {}"+ e.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException("JWT token is unsupported: {}"+ e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("JWT claims string is empty: {}"+ e.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot set user authentication: {}"+ e);
        }
        filterChain.doFilter(request, response);
    }



    private static final String AUTH_HEADER_NAME = "auth-tgh";
    private static final String JWT_PREFIX = "jwt ";

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(AUTH_HEADER_NAME);
        if (headerAuth != null && StringUtils.hasText(headerAuth) && headerAuth.startsWith(JWT_PREFIX)) {
            return headerAuth.substring(JWT_PREFIX.length());
        }
        return null;
    }



}
