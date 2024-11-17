package com.ulsa.oaxaca.edu.proyecto_banco.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Sucursal;

@Repository
public interface SucursalRepository extends CrudRepository<Sucursal, Long> {
    @Query("SELECT s FROM Sucursal s WHERE s.id NOT IN (SELECT p.sucursal.id FROM Personal p WHERE TYPE(p) = Gerente)")
    List<Sucursal> findSucursalesSinGerente();
}