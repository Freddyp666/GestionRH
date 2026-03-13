package com.crud.card.gestionrh.api.controller;


import com.crud.card.gestionrh.api.dto.request.LoginRequest;
import com.crud.card.gestionrh.api.dto.request.RegisterRequest;
import com.crud.card.gestionrh.api.dto.response.MensajeResponse;
import com.crud.card.gestionrh.domain.repository.RoleRepository;
import com.crud.card.gestionrh.domain.repository.UsuarioRepository;
import com.crud.card.gestionrh.infrastructure.security.jwt.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthController {

    //Dependincias inyectatas
    private final AuthenticationManager authenticationManager;//Manejo de autenticacion
    private final UsuarioRepository usuarioRepository;//acceso a usuarios
    private final RoleRepository roleRepository;//Acceso a roles
    private final PasswordEncoder passwordEncoder;//Encripta contrsenias
    private final JwtTokenProvider jwtTokenProvider;//Genera Tokens JWT


    //login
    /*@PostMapping(/login)
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try{
            //

        }catch (Exception e){
            e.printStackTrace();
        }
    }*/


    //Register
   /* @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {

        try {
            //Verificar si el usuario existe
            if (usuarioRepository.existsByUsername(registerRequest.getUsername())) {
                return ResponseEntity.badRequest().body(new MensajeResponse("En nombre de usuario ya existe"));
            }

            //Verifica si email ya existe
            if (usuarioRepository.existsByEmail(registerRequest.getEmail())) {
                return ResponseEntity.badRequest().body(new MensajeResponse("El email ya esta registrado"));
            }

            //Buscar el rol por nombre

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


}
