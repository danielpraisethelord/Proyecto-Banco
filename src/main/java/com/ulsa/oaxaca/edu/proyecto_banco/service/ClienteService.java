package com.ulsa.oaxaca.edu.proyecto_banco.service;

import java.util.*;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Cliente;

public interface ClienteService {

    Cliente save(Cliente cliente);

    List<Cliente> findAll();

    Optional<Cliente> findById(Long id);

    Optional<Cliente> update(Long id, Cliente cliente);

    Optional<Cliente> delete(Long id);
}
