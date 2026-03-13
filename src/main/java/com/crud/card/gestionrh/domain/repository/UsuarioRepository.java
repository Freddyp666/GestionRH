package com.crud.card.gestionrh.domain.repository;

import com.crud.card.gestionrh.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //Busca un usuario por su username//usando el login, verificar si existe
    Optional<Usuario> findByUsername(String username);

    //Busca un usuario por su email//usando el registro, verificar si existe
    Optional<Usuario> findByEmail(String email);

    //Verifica si el usuario existe por username|
    boolean existsByUsername(String username);

    //Verifica si el usuario existe por email
    boolean existsByEmail(String email);

    // Buscar todos los usuarios de un rol específico
    // Spring entiende: "findAll" + "By" + "Role" + "Name"
    List<Usuario> findAllByRoleName(String roleName);

    /**
     * Busca usuario por username y carga su rol (evita LazyInitializationException)
     * Usado en: Autenticación cuando necesitas el rol
     */
    @Query("SELECT u FROM Usuario u JOIN FETCH u.role WHERE u.username = :username")
    Optional<Usuario> findByUsernameWithRole(@Param("username") String username);


    /**
     * Actualiza el contador de intentos fallidos de login
     * Usado en: Seguridad - control de intentos fallidos
     */
    @Modifying  // ← Indica que es una operación de modificación (UPDATE/DELETE)
    @Transactional  // ← Necesario para operaciones de modificación
    @Query("UPDATE Usuario u SET u.failedAttempts = :attempts WHERE u.username = :username")
    void updateFailedAttempts(@Param("attempts") int attempts, @Param("username") String username);



    // Bloquear cuenta (account_locked = true)
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.accountLocked = true WHERE u.username = :username")
    void lockUser(@Param("username") String username);

    // Desbloquear cuenta y resetear intentos
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.accountLocked = false, u.failedAttempts = 0 WHERE u.username = :username")
    void unlockUser(@Param("username") String username);

    // Actualizar fecha de modificación
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.updatedAt = CURRENT_TIMESTAMP WHERE u.username = :username")
    void updateTimestamp(@Param("username") String username);


    //Buscar usuario bloqueado
    List<Usuario> findByAccountLockedTrue();

    //Buscar usuario habilitados
    List<Usuario> findByEnabledTrue();


    // Buscar usuarios con intentos fallidos mayores a X
    List<Usuario> findByFailedAttemptsGreaterThan(int attempts);


}
