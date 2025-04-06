package com.example.task_manager.controller;

import com.example.task_manager.model.User;
import com.example.task_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controlador para manejar la autenticación y registro de usuarios.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param user El usuario a registrar.
     * @return ResponseEntity con el usuario registrado o un error si el nombre de usuario ya existe.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Validar que el usuario no sea nulo
        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Username and password are required");
        }

        // Verificar si el nombre de usuario ya existe
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        // Encriptar la contraseña y guardar el usuario
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }
}