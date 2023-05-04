package com.revature.config;

import com.revature.service.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// this filter will intercept any incoming requests and ensure that they have a valid JWT token,
// makes sure that the user is authenticated
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {
        // get the Authorization header:
        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null, jwtToken = null;

        // if we have a valid token header:
        if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer " )) {
            // we have to remove the prefix "Bearer", we just want the token:
            jwtToken = requestTokenHeader.substring(7);
            try {
                // try to get the username from the token:
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        }

        // Once we get the token, validate it
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // using our service:
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            // if the token is valid, we configure Spring Security to manually set authentication
            if(jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            }
        }
        chain.doFilter(request, response);
    }

}
