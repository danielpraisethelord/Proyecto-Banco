package com.ulsa.oaxaca.edu.proyecto_banco.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Transaccion;

@Repository
public interface TransaccionRepository extends CrudRepository<Transaccion, Long> {

}
