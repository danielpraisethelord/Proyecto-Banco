package com.ulsa.oaxaca.edu.proyecto_banco.service;

import java.util.*;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Gerente;

public interface GerenteService {

    Gerente save(Gerente gerente);

    List<Gerente> findAll();

    Optional<Gerente> findById(Long id);

    Optional<Gerente> update(Long id, Gerente gerente);

    Optional<Gerente> delete(Long id);

    Boolean existsBySucursalId(Long sucursalId);

}
