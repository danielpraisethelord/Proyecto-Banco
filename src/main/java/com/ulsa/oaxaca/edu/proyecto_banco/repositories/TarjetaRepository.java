package com.ulsa.oaxaca.edu.proyecto_banco.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Tarjeta;

import jakarta.transaction.Transactional;

@Repository
public interface TarjetaRepository extends CrudRepository<Tarjeta, Long> {

    @Transactional
    @Query("SELECT t.saldoActual FROM Tarjeta t WHERE t.id = :tarjetaId")
    double obtenerSaldoActual(@Param("tarjetaId") Long tarjetaById);

    @Modifying
    @Query("UPDATE Tarjeta t SET t.saldoActual = t.saldoActual - :monto, t.limiteCredito = t.limiteCredito + :monto WHERE t.id = :id AND t.saldoActual >= :monto")
    int pagarTarjeta(@Param("id") Long id, @Param("monto") Double monto);
}
