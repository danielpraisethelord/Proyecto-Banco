package com.ulsa.oaxaca.edu.proyecto_banco.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Gerente;
import com.ulsa.oaxaca.edu.proyecto_banco.entities.Sucursal;
import com.ulsa.oaxaca.edu.proyecto_banco.entities.User;
import com.ulsa.oaxaca.edu.proyecto_banco.repositories.GerenteRepository;
import com.ulsa.oaxaca.edu.proyecto_banco.repositories.RoleRepository;
import com.ulsa.oaxaca.edu.proyecto_banco.repositories.SucursalRepository;
import com.ulsa.oaxaca.edu.proyecto_banco.service.GerenteService;
import com.ulsa.oaxaca.edu.proyecto_banco.service.UserService;

@Service
public class GerenteServiceImpl implements GerenteService {

    @Autowired
    private GerenteRepository gerenteRepository;

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public Gerente save(Gerente gerente) {
        if (gerente.getSucursal() != null && gerente.getSucursal().getId() != null) {
            Optional<Sucursal> sucursalOptional = sucursalRepository.findById(gerente.getSucursal().getId());
            sucursalOptional.ifPresent(gerente::setSucursal);
        }

        Gerente gerenteDb = gerenteRepository.save(gerente);

        User user = User.builder()
                .username(gerente.getRfc())
                .password(passwordEncoder.encode(gerente.getPassword()))
                .role(roleRepository.findByName("ROLE_GERENTE").orElseThrow())
                .persona(gerenteDb)
                .build();

        userService.save(user);

        return gerenteDb;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Gerente> findAll() {
        return (List<Gerente>) gerenteRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Gerente> findById(Long id) {
        return gerenteRepository.findById(id);
    }

    @Transactional
    @Modifying
    @Override
    public Optional<Gerente> update(Long id, Gerente gerente) {
        Optional<Gerente> gerenteOptional = gerenteRepository.findById(id);
        if (gerenteOptional.isPresent()) {
            Gerente gerenteDb = gerenteOptional.orElseThrow();
            gerenteDb.setNombre(gerente.getNombre());
            gerenteDb.setApellidoPaterno(gerente.getApellidoPaterno());
            gerenteDb.setApellidoMaterno(gerente.getApellidoMaterno());
            gerenteDb.setFechaNacimiento(gerente.getFechaNacimiento());
            gerenteDb.setGenero(gerente.getGenero());
            gerenteDb.setTelefono(gerente.getTelefono());
            gerenteDb.setEmail(gerente.getEmail());
            gerenteDb.setDireccion(gerente.getDireccion());
            gerenteDb.setRfc(gerente.getRfc());
            gerenteDb.setFechaContratacion(gerente.getFechaContratacion());
            gerenteDb.setTurno(gerente.getTurno());
            gerenteDb.setAniosDeExperiencia(gerente.getAniosDeExperiencia());
            gerenteDb.setHorasDeTrabajo(gerente.getHorasDeTrabajo());
            gerenteDb.setEstado(gerente.getEstado());
            gerenteDb.setSucursal(gerente.getSucursal());
            gerenteDb.setResponsabilidades(gerente.getResponsabilidades());
            gerenteDb.setSueldo(gerente.getSueldo());
            gerenteDb.setCertificaciones(gerente.getCertificaciones());
            gerenteDb.setProyectosActuales(gerente.getProyectosActuales());
            return Optional.of(gerenteRepository.save(gerenteDb));
        }
        return Optional.empty();
    }

    @Transactional
    @Modifying
    @Override
    public Optional<Gerente> delete(Long id) {
        Optional<Gerente> gerenteOptional = gerenteRepository.findById(id);
        gerenteOptional.ifPresent(gerenteRepository::delete);
        return gerenteOptional;
    }

    @Override
    public Boolean existsBySucursalId(Long sucursalId) {
        return gerenteRepository.existsBySucursalId(sucursalId);
    }

}
