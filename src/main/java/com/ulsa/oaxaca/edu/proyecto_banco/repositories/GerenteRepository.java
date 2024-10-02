package com.ulsa.oaxaca.edu.proyecto_banco.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Gerente;

@Repository
public interface GerenteRepository extends CrudRepository<Gerente, Long> {
    @Query("SELECT COUNT(g) > 0 FROM Gerente g WHERE g.sucursal.id = :sucursalId")
    boolean existsBySucursalId(@Param("sucursalId") Long sucursalId);
}
