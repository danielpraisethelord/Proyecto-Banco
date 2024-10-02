package com.ulsa.oaxaca.edu.proyecto_banco.service;

import java.util.*;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Persona;

public interface PersonaService {

    Persona save(Persona persona);

    List<Persona> findAll();

    Optional<Persona> findById(Long id);

    Optional<Persona> update(Long id, Persona persona);

    Optional<Persona> delete(Long id);
}
