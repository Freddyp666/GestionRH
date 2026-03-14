package com.crud.card.gestionrh.domain.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;//aqui busacamo el rol por nombre

    private String description;

    @OneToMany(mappedBy = "role")
    private List<Usuario> usuarios = new ArrayList<>();

}
