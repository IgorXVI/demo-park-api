package com.compass.demo_park_api.jwt;

import com.compass.demo_park_api.entities.User;
import com.compass.demo_park_api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByUsername(username);
        return new JwtUserDetails(user);
    }

    public JwtToken getTokenAuthenticated(String username) {
        User.Role role = userService.getRoleByUsername(username);
        return JwtUtils.createToken(username, role.name().substring("ROLE_".length()));
    }
}