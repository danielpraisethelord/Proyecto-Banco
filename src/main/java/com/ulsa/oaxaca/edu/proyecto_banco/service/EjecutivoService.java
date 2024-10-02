package com.ulsa.oaxaca.edu.proyecto_banco.service;

import java.util.*;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Ejecutivo;

public interface EjecutivoService {

    Ejecutivo save(Ejecutivo ejecutivo);

    List<Ejecutivo> findAll();

    Optional<Ejecutivo> findById(Long id);

    Optional<Ejecutivo> update(Long id, Ejecutivo ejecutivo);

    Optional<Ejecutivo> delete(Long id);
}
