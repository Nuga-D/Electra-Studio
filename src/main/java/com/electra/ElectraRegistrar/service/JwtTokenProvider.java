package com.electra.ElectraRegistrar.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtTokenProvider {

//    public JwtTokenProvider(String jwtSecret, int jwtExpiration) {
//        this.jwtSecret = jwtSecret;
//        this.jwtExpiration = jwtExpiration;
//    }

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpiration;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;



    public String generateToken(Authentication authentication) {
        // ...

        //UserDetails userDetails = (UserDetails) authentication.getDetails();
        //UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

        //String username = userDetails.getUsername();

        String username = authentication.getName();
        Claims claims = Jwts.claims().setSubject(username);
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtExpiration * 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

}
