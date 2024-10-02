package com.ulsa.oaxaca.edu.proyecto_banco.repositories;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.BanamexOaxaca;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BanamexOaxacaRepository extends CrudRepository<BanamexOaxaca, Long> {

}
