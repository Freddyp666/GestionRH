package com.crud.card.gestionrh.api.controller;

import com.crud.card.gestionrh.api.dto.response.JwtResponse;
import org.springframework.http.HttpStatus;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

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
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println("🔵 Intento de login: " + loginRequest.getEmail());
        try {

            //  Autenticar el usuario
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            //  Guardar la autenticacion en el contexto
            SecurityContextHolder.getContext().setAuthentication(authentication);

            //  Generar token
            String token = jwtTokenProvider.generarToken(authentication);

            // Obtener usuario
            Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
              .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            //Obtener el userioa de la base de datos
            String realUser = usuario.getUsername();

            //Resetear intetnos fallidos (Login exitoso)
            usuarioRepository.updateFailedAttempts(0, realUser);

            //Actualizar ultima fecha de acceso
            usuario.setUpdatedAt(LocalDateTime.now());
            usuarioRepository.save(usuario);

            //Crear lista de roles para el fronted
            List<String> roles = Collections.singletonList(usuario.getRole().getName());
            System.out.println("🟢 Autenticación exitosa para: " + loginRequest.getEmail());

            //  Devolver respuesta (crear JwtResponse)
            return ResponseEntity.ok(new JwtResponse(
              token,
              usuario.getId(),
              usuario.getUsername(),
              usuario.getEmail(),
              roles
            ));


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MensajeResponse("Credenciales inválidas"+e.getMessage()));
        }
    }



    //Register
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {

        try {
            //Verificar si el usuario existe
            if (usuarioRepository.existsByUsername(registerRequest.getUsername())) {
                return ResponseEntity.badRequest().body(new MensajeResponse("En nombre de usuario ya existe", false));
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
            return ResponseEntity.status(HttpStatus.CREATED).body(new MensajeResponse("Usuario registrado correctamente"));

        } catch (Exception e) {
            e.printStackTrace();

            //devolver la respuesta del error
            return ResponseEntity.badRequest().body(new MensajeResponse("Error interno del servidor al registro de usuario" + e.getMessage()));
        }


    }


}
