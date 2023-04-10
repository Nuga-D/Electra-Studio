package com.electra.service;

import com.electra.models.Company;
import com.electra.models.User;
import com.electra.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
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

    @Autowired
    private UserRepository userRepository;



    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

       User user = userRepository.findByEmail(username);

       String firstName = user.getFirstName();
       String lastName = user.getLastName();
       String email = user.getEmail();
       Company company = user.getCompany();
       String companyName = company.getName();


        Claims claims = Jwts.claims().setSubject(username);
        claims.put("email", email);
        claims.put("firstName", firstName);
        claims.put("lastName", lastName);
        claims.put("companyName", companyName);


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
