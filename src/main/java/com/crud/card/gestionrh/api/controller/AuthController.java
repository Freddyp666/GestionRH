package com.crud.card.gestionrh.api.controller;

import org.springframework.http.HttpStatus;  // ← Agrega esto arriba
import com.crud.card.gestionrh.api.dto.request.LoginRequest;
import com.crud.card.gestionrh.api.dto.request.RegisterRequest;
import com.crud.card.gestionrh.api.dto.response.MensajeResponse;

import com.crud.card.gestionrh.domain.model.Role;
import com.crud.card.gestionrh.domain.model.Usuario;
import com.crud.card.gestionrh.domain.repository.RoleRepository;
import com.crud.card.gestionrh.domain.repository.UsuarioRepository;
import com.crud.card.gestionrh.infrastructure.security.jwt.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    //Dependincias inyectatas
    private final AuthenticationManager authenticationManager;//Manejo de autenticacion
    private final UsuarioRepository usuarioRepository;//acceso a usuarios
    private final RoleRepository roleRepository;//Acceso a roles
    private final PasswordEncoder passwordEncoder;//Encripta contrsenias
    private final JwtTokenProvider jwtTokenProvider;//Genera Tokens JWT


    //login
    /*@PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // 1. Autenticar
            Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
              )
            );

            // 2. Generar token
            String token = jwtTokenProvider.generateToken(authentication);

            // 3. Obtener usuario
            Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername())
              .orElseThrow();

            // 4. Devolver respuesta (necesitas crear JwtResponse)
            return ResponseEntity.ok(new JwtResponse(
              token,
              usuario.getId(),
              usuario.getUsername(),
              usuario.getEmail(),
              Collections.singletonList(usuario.getRole().getName())
            ));

        } catch (Exception e) {
            return ResponseEntity
              .status(HttpStatus.UNAUTHORIZED)
              .body(new MensajeResponse("Credenciales inválidas"));
        }
    }*/


    //Register
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {

        try {
            //Verificar si el usuario existe
            if (usuarioRepository.existsByUsername(registerRequest.getUsername())) {
                return ResponseEntity.badRequest().body(new MensajeResponse("En nombre de usuario ya existe",false));
            }

            //Verifica si email ya existe
            if (usuarioRepository.existsByEmail(registerRequest.getEmail())) {
                return ResponseEntity.badRequest().body(new MensajeResponse("El email ya esta registrado", false));
            }

            //Buscar el rol por nombre
            Role roleEmpleado = roleRepository.findByName("EMPLOYEE").orElseThrow(() -> new RuntimeException("ERROR: ROL DE EMPLEADO NO ENCONTRADO"));


            //Crear el usario nuevo para el registro
            Usuario usuario = new Usuario();
            usuario.setUsername(registerRequest.getUsername());
            usuario.setEmail(registerRequest.getEmail());
            usuario.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            usuario.setEnabled(true);
            usuario.setAccountLocked(false);
            usuario.setFailedAttempts(0);
            usuario.setCreatedAt(LocalDateTime.now());
            usuario.setRole(roleEmpleado);//automatico

            usuarioRepository.save(usuario);
            return ResponseEntity
              .status(HttpStatus.CREATED)
              .body(new MensajeResponse("Usuario registrado correctamente"));

        } catch (Exception e) {
            e.printStackTrace();

            //devolver la respuesta del error
            return ResponseEntity.badRequest().body(new MensajeResponse("Error interno del servidor al registro de usuario"+e.getMessage()));
        }


    }


}
