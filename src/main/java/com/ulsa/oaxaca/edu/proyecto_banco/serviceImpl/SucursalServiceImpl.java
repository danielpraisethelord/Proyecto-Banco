package com.ulsa.oaxaca.edu.proyecto_banco.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Sucursal;
import com.ulsa.oaxaca.edu.proyecto_banco.repositories.SucursalRepository;
import com.ulsa.oaxaca.edu.proyecto_banco.service.SucursalService;

@Service
public class SucursalServiceImpl implements SucursalService {

    @Autowired
    private SucursalRepository sucursalRepository;

    @Transactional
    @Override
    public Sucursal save(Sucursal sucursal) {
        return sucursalRepository.save(sucursal);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Sucursal> findAll() {
        return (List<Sucursal>) sucursalRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Sucursal> findById(Long id) {
        return sucursalRepository.findById(id);
    }

    @Transactional
    @Modifying
    @Override
    public Optional<Sucursal> update(Long id, Sucursal sucursal) {
        Optional<Sucursal> sucursalOptional = sucursalRepository.findById(id);
        if (sucursalOptional.isPresent()) {
            Sucursal sucursalDb = sucursalOptional.orElseThrow();
            sucursalDb.setNombre(sucursal.getNombre());
            sucursalDb.setDireccion(sucursal.getDireccion());
            sucursalDb.setTelefono(sucursal.getTelefono());
            sucursalDb.setEmail(sucursal.getEmail());
            sucursalDb.setHorarioAtencion(sucursal.getHorarioAtencion());
            sucursalDb.setFechaApertura(sucursal.getFechaApertura());
            sucursalDb.setEstado(sucursal.getEstado());
            sucursalDb.setBanco(sucursal.getBanco());
            sucursalDb.setPersonal(sucursal.getPersonal());
            sucursalDb.setClientes(sucursal.getClientes());
            return Optional.of(sucursalRepository.save(sucursalDb));
        }
        return Optional.empty();
    }

    @Transactional
    @Modifying
    @Override
    public Optional<Sucursal> delete(Long id) {
        Optional<Sucursal> sucursal = sucursalRepository.findById(id);
        sucursal.ifPresent(sucursalRepository::delete);
        return sucursal;
    }

    @Override
    public Map<Object, String> findSucursalesSinGerente() {
        List<Sucursal> sucursales = sucursalRepository.findSucursalesSinGerente();
        Map<Object, String> sucursalesMap = sucursales.stream()
                .collect(Collectors.toMap(Sucursal::getId, Sucursal::getNombre));
        return sucursalesMap;
    }

    @Override
    public Map<Object, String> findAllNameId() {
        List<Sucursal> sucursales = (List<Sucursal>) sucursalRepository.findAll();
        Map<Object, String> sucursalesMap = sucursales.stream()
                .collect(Collectors.toMap(Sucursal::getId, Sucursal::getNombre));
        return sucursalesMap;
    }
}
