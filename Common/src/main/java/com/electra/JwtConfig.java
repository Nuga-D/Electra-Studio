package com.electra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpiration;

//    @Bean
//    public JwtTokenProvider jwtTokenProvide() {
//        return new JwtTokenProvider(jwtSecret, jwtExpiration);
//    }

}

