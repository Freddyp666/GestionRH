package com.crud.card.gestionrh.domain.repository;


import com.crud.card.gestionrh.domain.model.Role_db;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role_db, Long> {

    Optional<Role_db> findByRole(String name);

    boolean existsByRole(String name);

}
