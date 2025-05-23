package eci.ieti.safezone.controller;

import eci.ieti.safezone.model.User;
import eci.ieti.safezone.model.Role;
import eci.ieti.safezone.service.UserService;
import eci.ieti.safezone.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody User user) {
        user.setRole(Role.USER);
        userService.save(user);
        return ResponseEntity.status(201).body("Usuario registrado exitosamente");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        User user = userService.validateUser(username, password);
        if (user == null) {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
