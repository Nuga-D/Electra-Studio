package com.electra.ElectraRegistrar.controller;

import com.electra.ElectraRegistrar.models.*;
import com.electra.ElectraRegistrar.repository.UserRepository;
import com.electra.ElectraRegistrar.service.JwtTokenProvider;
import com.electra.ElectraRegistrar.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.security.sasl.AuthenticationException;
import java.util.Date;
import java.util.List;

@RestController
public class ERAuthController {

    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginForm) {
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
        String token = jwtTokenProvider.generateToken((Authentication) userDetails);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        if (userDetailsServiceImpl.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Username is already taken!"));
        }

        String hashPwd = passwordEncoder.encode(signupRequest.getPassword());
        User user = new User(signupRequest.getFirstName(), signupRequest.getLastName(), signupRequest.getEmail(), hashPwd, signupRequest.getRole());
        userDetailsServiceImpl.save(user);

        return ResponseEntity.ok(new SuccessResponse("User registered successfully!"));
    }
}
