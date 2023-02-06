package config;

import lombok.extern.slf4j.Slf4j;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ERAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        List<User> user = userRepository.findByEmail(username);
        if (user.size() > 0) {
//            if (user.get(0).getDisabled()) {
//                throw new UsernameNotFoundException("User has been disabled");
//            } else {
                if (passwordEncoder.matches(password, user.get(0).getPassword())) {
                    log.info(username + ":::  Successfully logged in");
                    return new UsernamePasswordAuthenticationToken(username, password, getGrantedAuthorities(user.get(0).getRole()));
                } else {
                    throw new BadCredentialsException("Invalid password!");
                }
//            }
        } else {
            throw new BadCredentialsException("Mo user registered with this details!");
        }
    }

    private List<SimpleGrantedAuthority> getGrantedAuthorities(String authorities) {
        String[] roles = authorities.split(",");
        return Arrays.stream(roles)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
