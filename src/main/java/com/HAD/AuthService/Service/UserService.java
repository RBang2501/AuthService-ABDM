package com.HAD.AuthService.Service;

import com.HAD.AuthService.Model.User;
import com.HAD.AuthService.DAO.UserDAO;
import com.HAD.AuthService.JWT.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDAO.save(user);
    }

    public String signIn(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        
        User user = userDAO.findByEmail(email);
        if (authentication.isAuthenticated()) {
            String token = jwtTokenProvider.generateToken((User) authentication.getPrincipal());
            user.setJwtToken(token); 
            if (user.getJwtToken() == null || user.getJwtToken().isEmpty() || user.getJwtToken().equals("NAN")) {
                userDAO.save(user);
            }
            else 
            {
                userDAO.update(user);
            }
            return token;
        }
        throw new UsernameNotFoundException("Invalid credentials.");
    }

    public String refreshToken(String refreshToken) {
        String email = jwtTokenProvider.extractUsername(refreshToken);
        User user = userDAO.findByEmail(email);
        if (user != null && jwtTokenProvider.isTokenValid(refreshToken, user)) {
            return jwtTokenProvider.generateToken(user);
        }
        throw new IllegalArgumentException("Invalid refresh token.");
    }

    public User getUserByToken(String jwtToken) {
        return userDAO.findByToken(jwtToken);
    }

}
