package com.ulsa.oaxaca.edu.proyecto_banco.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Cajero;

@Repository
public interface CajeroRepository extends CrudRepository<Cajero, Long> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cajero c WHERE c.cajaAsignada = :cajaAsignada AND c.sucursal.id = :sucursalId")
    boolean isCajaOcupada(@Param("cajaAsignada") Integer cajaAsignada, @Param("sucursalId") Long sucursalId);
}
