package com.ulsa.oaxaca.edu.proyecto_banco.service;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Cajero;

import java.util.List;
import java.util.Optional;

public interface CajeroSerice {

    Cajero save(Cajero cajero);

    List<Cajero> findAll();

    Optional<Cajero> findById(Long id);

    Optional<Cajero> update(Long id, Cajero cajero);

    Optional<Cajero> delete(Long id);

    Boolean isCajaOcupada(Integer cajaAsignada, Long sucursalId);
}
