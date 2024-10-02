package com.ulsa.oaxaca.edu.proyecto_banco.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Cuenta;

import jakarta.transaction.Transactional;

@Repository
public interface CuentaRepository extends CrudRepository<Cuenta, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Cuenta c SET c.saldoActual = c.saldoActual + :monto WHERE c.id = :id")
    int depositar(@Param("id") Long id, @Param("monto") Double monto);

    @Transactional
    @Query("SELECT c.saldoActual FROM Cuenta c WHERE c.id = :cuentaId")
    double obtenerSaldoActual(@Param("cuentaId") Long cuentaById);

    @Transactional
    @Modifying
    @Query("UPDATE Cuenta c SET c.saldoActual = c.saldoActual - :monto WHERE c.id = :id AND c.saldoActual >= :monto")
    int retirar(@Param("id") Long id, @Param("monto") Double monto);

}
