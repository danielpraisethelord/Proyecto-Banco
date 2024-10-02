package com.ulsa.oaxaca.edu.proyecto_banco.serviceImpl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ulsa.oaxaca.edu.proyecto_banco.dto.CuentaDto;
import com.ulsa.oaxaca.edu.proyecto_banco.dto.TarjetaDto;
import com.ulsa.oaxaca.edu.proyecto_banco.entities.Cuenta;
import com.ulsa.oaxaca.edu.proyecto_banco.repositories.CuentaRepository;
import com.ulsa.oaxaca.edu.proyecto_banco.repositories.TarjetaRepository;
import com.ulsa.oaxaca.edu.proyecto_banco.service.CuentaService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CuentaServiceImpl implements CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private TarjetaRepository tarjetaRepository;

    @Transactional
    @Override
    public Cuenta save(Cuenta cuenta) {
        cuenta.setFechaApertura(LocalDate.now());
        cuenta.setSaldoActual(0.00);
        return cuentaRepository.save(cuenta);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Cuenta> findAll() {
        return (List<Cuenta>) cuentaRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Cuenta> findById(Long id) {
        return cuentaRepository.findById(id);
    }

    @Transactional
    @Modifying
    @Override
    public Optional<Cuenta> update(Long id, Cuenta cuenta) {
        Optional<Cuenta> cuentaOptional = cuentaRepository.findById(id);
        if (cuentaOptional.isPresent()) {
            Cuenta cuentaDb = cuentaOptional.orElseThrow();
            cuentaDb.setFechaApertura(cuenta.getFechaApertura());
            cuentaDb.setTipoCuenta(cuenta.getTipoCuenta());
            cuentaDb.setMoneda(cuenta.getMoneda());
            cuentaDb.setSaldoActual(cuenta.getSaldoActual());
            cuentaDb.setCliente(cuenta.getCliente());
            cuentaDb.setTarjetas(cuenta.getTarjetas());
            cuentaDb.setTransacciones(cuenta.getTransacciones());
            return Optional.of(cuentaRepository.save(cuentaDb));
        }
        return Optional.empty();
    }

    @Transactional
    @Modifying
    @Override
    public Optional<Cuenta> delete(Long id) {
        Optional<Cuenta> cuentaOptional = cuentaRepository.findById(id);
        cuentaOptional.ifPresent(cuentaRepository::delete);
        return cuentaOptional;
    }

    @Transactional
    @Modifying
    @Override
    public Optional<CuentaDto> depositar(Long id, Double monto) {
        int updateRows = cuentaRepository.depositar(id, monto);
        if (updateRows == 1) {
            Optional<Cuenta> cuentaOpt = cuentaRepository.findById(id);
            if (cuentaOpt.isPresent()) {
                return Optional.of(convertirACuentaDTO(cuentaOpt.get()));
            }
        }
        return null;
    }

    @Transactional
    @Modifying
    @Override
    public ResponseEntity<?> retirar(Long id, Double monto) {
        double saldoActual = cuentaRepository.obtenerSaldoActual(id);
        if (saldoActual < monto) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Saldo insuficiente");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        int updateRows = cuentaRepository.retirar(id, monto);
        if (updateRows == 1) {
            Optional<Cuenta> cuentaOpt = cuentaRepository.findById(id);
            if (cuentaOpt.isPresent()) {
                return new ResponseEntity<>(convertirACuentaDTO(cuentaOpt.get()), HttpStatus.OK);
            }
        }
        return null;
    }

    @Override
    @Transactional
    @Modifying
    public ResponseEntity<?> transferir(Long idOrigen, Long idDestino, Double monto) {
        double saldoActual = cuentaRepository.obtenerSaldoActual(idOrigen);
        if (saldoActual >= monto) {
            int rowsUpdateOrigen = cuentaRepository.retirar(idOrigen, monto);
            if (rowsUpdateOrigen == 1) {
                int rowsUpdateDestino = cuentaRepository.depositar(idDestino, monto);
                if (rowsUpdateDestino == 1) {
                    Optional<Cuenta> cuentaOpt = cuentaRepository.findById(idOrigen);
                    if (cuentaOpt.isPresent()) {
                        return new ResponseEntity<>(convertirACuentaDTO(cuentaOpt.get()), HttpStatus.OK);
                    }
                }
            }
        }
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Saldo insuficiente o cuentas no encontradas");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    @Transactional
    @Modifying
    public ResponseEntity<Object> pagar(Long idTarjeta, Double monto) {
        double saldoActual = tarjetaRepository.obtenerSaldoActual(idTarjeta);
        Map<String, String> response = new HashMap<>();

        if (monto >= saldoActual) {
            int rowsUpdated = tarjetaRepository.pagarTarjeta(idTarjeta, monto);
            if (rowsUpdated == 1) {
                response.put("mensaje", "Pago realizado con éxito");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } else {
            int rowsUpdated = tarjetaRepository.pagarTarjeta(idTarjeta, monto);
            if (rowsUpdated == 1) {
                response.put("mensaje", "Pago parcial realizado. Saldo insuficiente para completar el pago.");
                response.put("montoPagado", String.valueOf(monto));
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }

        response.put("mensaje", "Error al realizar el pago");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private CuentaDto convertirACuentaDTO(Cuenta cuenta) {
        List<TarjetaDto> tarjetasDto = cuenta.getTarjetas().stream()
                .map(tarjeta -> new TarjetaDto(
                        tarjeta.getId(),
                        tarjeta.getNumeroTarjeta(),
                        tarjeta.getTipo(),
                        tarjeta.getEstado(),
                        tarjeta.getFechaExpiracion(),
                        tarjeta.getLimiteCredito(),
                        tarjeta.getSaldoActual()))
                .collect(Collectors.toList());
        return new CuentaDto(
                cuenta.getId(),
                cuenta.getFechaApertura(),
                cuenta.getSaldoActual(),
                cuenta.getTipoCuenta(),
                cuenta.getMoneda(),
                tarjetasDto);
    }
}