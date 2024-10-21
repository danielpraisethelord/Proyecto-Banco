package com.ulsa.oaxaca.edu.proyecto_banco.repositories;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.BanamexOaxaca;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BanamexOaxacaRepository extends CrudRepository<BanamexOaxaca, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE BanamexOaxaca b SET b.numeroSucursales = b.numeroSucursales + 1 WHERE b.id = :id")
    void incrementNumeroSucursales(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE BanamexOaxaca b SET b.totalClientes = b.totalClientes + 1 WHERE b.id = :id")
    void incrementTotalClientes(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE BanamexOaxaca b SET b.numeroEmpleados = b.numeroEmpleados + 1 WHERE b.id = :id")
    void incrementNumeroEmpleados(@Param("id") Long id);
}
