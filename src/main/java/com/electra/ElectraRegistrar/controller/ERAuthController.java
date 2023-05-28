package com.electra.ElectraRegistrar.controller;

import com.electra.ElectraRegistrar.models.*;
import com.electra.ElectraRegistrar.repository.RoleRepository;
import com.electra.ElectraRegistrar.repository.UserRepository;
import com.electra.ElectraRegistrar.service.CompanyService;
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

//@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RestController
@RequestMapping("/auth")
public class ERAuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CompanyService companyService;

    @CrossOrigin(origins = "https://etaxpayer-syntechsysworld.vercel.app/session/signin", allowedHeaders = "POST", methods = {RequestMethod.GET, RequestMethod.POST})
    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginForm) {
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }


    @CrossOrigin(origins = "https://etaxpayer-syntechsysworld.vercel.app/session/signup", allowedHeaders = "POST", methods = {RequestMethod.GET, RequestMethod.POST})
    @PostMapping("/signup")
    public ResponseEntity registerUser(@RequestBody SignupRequest signupRequest) {

        String registerAs = signupRequest.getRegisterAs();

        String hashPwd = passwordEncoder.encode(signupRequest.getPassword());

        Set<Role> roles = new HashSet<>();

        if (userDetailsServiceImpl.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Username is already taken!"));
        } else if (signupRequest.getRole() == null) {
            String defaultRole = "USER";
            Role role = roleRepository.findByName(defaultRole)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

            roles.add(role);
        } else {
            Role role = roleRepository.findByName(signupRequest.getRole())
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

            roles.add(role);
        }

        if (registerAs.matches("company")) {

            Company company = signupRequest.getCompany();

            String companyName = company.getName();

            String companyAddress = company.getAddress();

            String companyRegNo = company.getRegistrationNumber();

            String companyTaxID = company.getTaxID();

            String companyRepPhoneNo = company.getRepresentativePhoneNo();

         if (companyService.companyExists(companyName)) {
            // company already exists, create user with existing company
            Company existingCompany = companyService.findByName(companyName).get();
            User user = new User(signupRequest.getFirstName(), signupRequest.getLastName(), signupRequest.getEmail(), hashPwd, signupRequest.getHomeAddress(), signupRequest.getPhoneNumber(), signupRequest.getNIN(), signupRequest.getRegisterAs(), existingCompany, roles);
            userDetailsServiceImpl.save(user);
            return ResponseEntity.ok(new SuccessResponse("User registered successfully"));
        } else {
            // company does not exist, create new company and user
            Company newCompany = new Company(companyName, companyAddress, companyRegNo, companyTaxID, companyRepPhoneNo);
            companyService.saveCompany(newCompany);
            User user = new User(signupRequest.getFirstName(), signupRequest.getLastName(), signupRequest.getEmail(), hashPwd, signupRequest.getHomeAddress(), signupRequest.getPhoneNumber(), signupRequest.getNIN(), signupRequest.getRegisterAs(), newCompany, roles);
            userDetailsServiceImpl.save(user);
            return ResponseEntity.ok(new SuccessResponse("User and company registered successfully"));
        }} else {
            User user = new User(signupRequest.getFirstName(), signupRequest.getLastName(), signupRequest.getEmail(), hashPwd, signupRequest.getHomeAddress(), signupRequest.getPhoneNumber(), signupRequest.getNIN(), signupRequest.getRegisterAs(), roles);
            userDetailsServiceImpl.save(user);
            return ResponseEntity.ok(new SuccessResponse("User registered successfully"));

        }

    }


}
