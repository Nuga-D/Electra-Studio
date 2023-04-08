package com.electra.ElectraRegistrar.controller;

import com.electra.ElectraRegistrar.models.*;
import com.electra.ElectraRegistrar.repository.RoleRepository;
import com.electra.ElectraRegistrar.repository.UserRepository;
import com.electra.ElectraRegistrar.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/allUsers")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userDetailsServiceImpl.getUserById(id).get();
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UpdateUser updatedUser) {
        Optional<User> userOptional = userDetailsServiceImpl.getUserById(id);
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOptional.get();

        Set<Role> roles = new HashSet<>();

        Role role = roleRepository.findByName(updatedUser.getRoles())
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        roles.add(role);

        Company company = updatedUser.getCompany();


        // Update the user fields
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        user.setHomeAddress(updatedUser.getHomeAddress());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        user.setNIN(updatedUser.getNIN());
       user.setCompany(company);
        user.setRoles(roles);
        // Update any other fields as needed

        userDetailsServiceImpl.saveUser(user);

        return ResponseEntity.ok(new SuccessResponse("User updated successfully!"));
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
            userRepository.save(user);
            return ResponseEntity.ok(new SuccessResponse("Password updated successfully!"));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("Old password is incorrect!"));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User existingUser = userDetailsServiceImpl.getUserById(id).get();
        Long userId = existingUser.getId();
        if (existingUser != null) {
            userDetailsServiceImpl.deleteUserById(userId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
