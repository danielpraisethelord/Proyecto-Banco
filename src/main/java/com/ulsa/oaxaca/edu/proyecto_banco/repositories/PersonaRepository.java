package com.ulsa.oaxaca.edu.proyecto_banco.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Persona;

@Repository
public interface PersonaRepository extends CrudRepository<Persona, Long> {

}
