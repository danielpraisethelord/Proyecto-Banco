package com.ulsa.oaxaca.edu.proyecto_banco.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.ulsa.oaxaca.edu.proyecto_banco.dto.CuentaDto;
import com.ulsa.oaxaca.edu.proyecto_banco.entities.Cuenta;

public interface CuentaService {

    Cuenta save(Cuenta cuenta);

    List<Cuenta> findAll();

    Optional<Cuenta> findById(Long id);

    Optional<Cuenta> update(Long id, Cuenta cuenta);

    Optional<Cuenta> delete(Long id);

    Optional<CuentaDto> depositar(Long id, Double monto);

    ResponseEntity<?> retirar(Long id, Double monto);

    ResponseEntity<?> transferir(Long idOrigen, Long idDestino, Double monto);

    ResponseEntity<?> pagar(Long idTarjeta, Double monto);
}