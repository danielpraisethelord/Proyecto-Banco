package com.ulsa.oaxaca.edu.proyecto_banco.service;

import java.util.List;
import java.util.Optional;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Personal;

public interface PersonalService {

    List<Personal> findAll();

    Optional<Personal> findById(Long id);
}
