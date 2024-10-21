package com.ulsa.oaxaca.edu.proyecto_banco.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
