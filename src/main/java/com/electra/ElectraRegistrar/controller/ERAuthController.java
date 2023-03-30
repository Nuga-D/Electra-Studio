package com.electra.ElectraRegistrar.controller;

import com.electra.ElectraRegistrar.models.*;
import com.electra.ElectraRegistrar.repository.RoleRepository;
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
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins ="*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RestController
public class ERAuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private RoleRepository roleRepository;

    @CrossOrigin(origins ="*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginForm) {
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

        if (userDetails instanceof User) {
            User user = (User) userDetails;
            String token = jwtTokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
            // handle the case where userDetails is not an instance of User
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @CrossOrigin(origins ="*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @PostMapping("/signup")
    public ResponseEntity registerUser(@RequestBody SignupRequest signupRequest) {
        if (userDetailsServiceImpl.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Username is already taken!"));
        }

        String hashPwd = passwordEncoder.encode(signupRequest.getPassword());
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(signupRequest.getRole())
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(role);
        User user = new User(signupRequest.getFirstName(), signupRequest.getLastName(), signupRequest.getEmail(), hashPwd, roles);
        userDetailsServiceImpl.save(user);

        return ResponseEntity.ok(new SuccessResponse("User registered successfully!"));
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
