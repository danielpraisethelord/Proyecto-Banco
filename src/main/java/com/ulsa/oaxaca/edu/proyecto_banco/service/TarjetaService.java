package com.ulsa.oaxaca.edu.proyecto_banco.service;

import java.util.*;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Tarjeta;

public interface TarjetaService {

    Tarjeta save(Tarjeta tarjeta);

    List<Tarjeta> findAll();

    Optional<Tarjeta> findById(Long id);

    Optional<Tarjeta> update(Long id, Tarjeta tarjeta);

    Optional<Tarjeta> delete(Long id);

    List<Tarjeta> findByClienteId(Long clienteId);

    Boolean existsByNumeroTarjeta(String numeroTarjeta);
}
