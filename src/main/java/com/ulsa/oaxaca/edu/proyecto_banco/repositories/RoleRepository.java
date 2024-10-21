package com.ulsa.oaxaca.edu.proyecto_banco.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
