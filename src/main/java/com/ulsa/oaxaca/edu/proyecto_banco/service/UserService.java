package com.ulsa.oaxaca.edu.proyecto_banco.service;

import java.util.List;
import java.util.Optional;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.User;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(Long id);

    User save(User user);
}
