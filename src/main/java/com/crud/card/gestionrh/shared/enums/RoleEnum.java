package com.crud.card.gestionrh.shared.enums;


import java.util.Arrays;
import java.util.List;

//Control de roles
public class RoleEnum {

    public static final String ADMIN = "ADMIN";
    public static final String RRHH = "RRHH";
    public static final String EMPLOYEE = "EMPLOYEE";
    public static final String GERENTE = "GERENTE";
    public static final String CONTADOR = "CONTADOR";
    public static final String SUPERVISOR = "SUPERVISOR";
    public static final String RECLUTADOR = "RECLUTADOR";

    // Roles que puede gestionar RRHH
    public static final List<String> RRHH_ALLOWED_ROLES = Arrays.asList(
      EMPLOYEE, GERENTE, SUPERVISOR, RECLUTADOR
    );

    // Roles que SOLO puede gestionar ADMIN
    public static final List<String> ADMIN_ONLY_ROLES = Arrays.asList(
      ADMIN, RRHH, CONTADOR
    );

}
