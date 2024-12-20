package com.ulsa.oaxaca.edu.proyecto_banco.service;

import java.util.Map;
import java.util.Optional;
import java.util.List;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Sucursal;

public interface SucursalService {

    Sucursal save(Sucursal sucursal);

    List<Sucursal> findAll();

    Map<Object, String> findAllNameId();

    Map<Object, String> findSucursalesSinGerente();

    Optional<Sucursal> findById(Long id);

    Optional<Sucursal> update(Long id, Sucursal sucursal);

    Optional<Sucursal> delete(Long id);
}
