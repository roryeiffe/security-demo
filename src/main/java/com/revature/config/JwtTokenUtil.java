package com.revature.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// This class is responsible for performing JWT operations like creation and validation
@Component
public class JwtTokenUtil implements Serializable {

    // 5 hours converted to seconds:
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    // get the value of our secret from the app.properties file:
    @Value("${jwt.secret}")
    private String secret;

    // A few things happening here
    // We set the claims, issue time and expiration time
    // issue time is right now, expiration is 5 hours from now
    // We sign the JWT using a certain algorithm and our secret key
    // Finally, we compact the token to a URL-safe string
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String subject = userDetails.getUsername();
        return Jwts.builder().setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }


    // using the secret key, get all claims from the token:
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }


    // A generic method for getting a specific claim from a token
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        // using the specific claimsResolver, we retrieve the specific claim we want
        // ex: if we passed in getSubject, we would get the subject or username from the token:
        return claimsResolver.apply(claims);
    }

    public String getUsernameFromToken(String token) {
        // Java method reference
        // https://www.javatpoint.com/java-8-method-reference
        // when this method reference gets passed to getClaimForToken, the resolve knows
        // to use the getSubject method
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // validate token with token and user details:
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
