package com.example.jwtdeneme.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
public class TokenGenerator {
    @Value("dereli")
    private String KEY;
    @Value("enoca")
    private String ISSUER;
    @Value("15")
    private long EXPIRES_ACCESS_TOKEN_MINUTE;

    public String generateToken(Authentication authentication){
    String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis()
                +(EXPIRES_ACCESS_TOKEN_MINUTE*60*1000)))
            .withIssuer(ISSUER)
                .sign(Algorithm.HMAC256(KEY.getBytes()));
    }

    public DecodedJWT verifyJWT(String token){
        Algorithm algorithm = Algorithm.HMAC256(KEY.getBytes());
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        try {
            return verifier.verify(token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token not verified", e);
        }
    }
}
