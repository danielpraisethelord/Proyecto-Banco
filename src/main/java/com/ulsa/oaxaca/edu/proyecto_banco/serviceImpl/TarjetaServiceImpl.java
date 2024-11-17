package com.ulsa.oaxaca.edu.proyecto_banco.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Tarjeta;
import com.ulsa.oaxaca.edu.proyecto_banco.repositories.TarjetaRepository;
import com.ulsa.oaxaca.edu.proyecto_banco.service.TarjetaService;

@Service
public class TarjetaServiceImpl implements TarjetaService {

    @Autowired
    private TarjetaRepository tarjetaRepository;

    @Transactional
    @Override
    public Tarjeta save(Tarjeta tarjeta) {
        tarjeta.setEstado("Activo");
        tarjeta.setSaldoActual(0.0);
        return tarjetaRepository.save(tarjeta);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Tarjeta> findAll() {
        return (List<Tarjeta>) tarjetaRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Tarjeta> findById(Long id) {
        return tarjetaRepository.findById(id);
    }

    @Transactional
    @Modifying
    @Override
    public Optional<Tarjeta> update(Long id, Tarjeta tarjeta) {
        return tarjetaRepository.findById(id).map(existingTarjeta -> {
            existingTarjeta.setEstado(tarjeta.getEstado());
            existingTarjeta.setTipo(tarjeta.getTipo());
            existingTarjeta.setCvc(tarjeta.getCvc());
            existingTarjeta.setFechaExpiracion(tarjeta.getFechaExpiracion());
            existingTarjeta.setNumeroTarjeta(tarjeta.getNumeroTarjeta());
            existingTarjeta.setLimiteCredito(tarjeta.getLimiteCredito());
            existingTarjeta.setSaldoActual(tarjeta.getSaldoActual());
            return tarjetaRepository.save(existingTarjeta);
        });
    }

    @Transactional
    @Modifying
    @Override
    public Optional<Tarjeta> delete(Long id) {
        return tarjetaRepository.findById(id).map(tarjeta -> {
            tarjetaRepository.delete(tarjeta);
            return tarjeta;
        });
    }

    public List<Tarjeta> findByClienteId(Long clienteId) {
        return tarjetaRepository.findByCuentaClienteId(clienteId);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existsByNumeroTarjeta(String numeroTarjeta) {
        return tarjetaRepository.existsByNumeroTarjeta(numeroTarjeta);
    }
}