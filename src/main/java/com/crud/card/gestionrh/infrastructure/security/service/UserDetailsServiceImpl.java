package com.crud.card.gestionrh.infrastructure.security.service;

import com.crud.card.gestionrh.domain.model.Usuario;
import com.crud.card.gestionrh.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Buscar usuario por username (con su rol)
        Usuario usuario = usuarioRepository.findByEmail(usernameOrEmail)
          .orElseGet(() -> usuarioRepository.findByUsername(usernameOrEmail)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario No encontrado: " + usernameOrEmail)));

        //Verificar si la cuenta está bloqueada
        if (usuario.getAccountLocked()) {
            throw new UsernameNotFoundException("Usuario no activo");
        }

        //Verificar si la cuenta está habilitada
        if (!usuario.getEnabled()) {
            throw new UsernameNotFoundException("Usuario deshabilitado");

        }

        // Convertir rol de tu BD al formato que Spring Security entiende
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRole().getName()//Ejemplo ROLE_ADMIN, ROLE_RRHH
        );

        return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, !usuario.getAccountLocked(), Collections.singletonList(authority)

        );

    }


}
