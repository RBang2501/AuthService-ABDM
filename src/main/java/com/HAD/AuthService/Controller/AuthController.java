package com.HAD.AuthService.Controller;

import com.HAD.AuthService.Model.User;
import com.HAD.AuthService.Service.UserService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User newUser = userService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signInUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        System.out.println(email);
        String token = userService.signIn(email, password);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<String> refreshToken(@RequestParam String refreshToken) {
        String token = userService.refreshToken(refreshToken);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/testToken")
    public String refreshToken() {
        return "Token Working !";
    }
}
