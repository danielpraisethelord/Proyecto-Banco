package com.ulsa.oaxaca.edu.proyecto_banco.serviceImpl;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Cajero;
import com.ulsa.oaxaca.edu.proyecto_banco.entities.User;
import com.ulsa.oaxaca.edu.proyecto_banco.repositories.CajeroRepository;
import com.ulsa.oaxaca.edu.proyecto_banco.repositories.RoleRepository;
import com.ulsa.oaxaca.edu.proyecto_banco.service.CajeroSerice;
import com.ulsa.oaxaca.edu.proyecto_banco.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CajeroServiceImpl implements CajeroSerice {

    @Autowired
    private CajeroRepository cajeroRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public Cajero save(Cajero cajero) {
        Cajero cajeroDb = cajeroRepository.save(cajero);

        User user = User.builder()
                .username(cajero.getRfc())
                .password(passwordEncoder.encode(cajero.getPassword()))
                .role(roleRepository.findByName("ROLE_CAJERO").orElseThrow())
                .persona(cajeroDb)
                .build();

        userService.save(user);

        return cajeroDb;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Cajero> findAll() {
        return (List<Cajero>) cajeroRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Cajero> findById(Long id) {
        return cajeroRepository.findById(id);
    }

    @Transactional
    @Modifying
    @Override
    public Optional<Cajero> update(Long id, Cajero cajero) {
        Optional<Cajero> cajeroOptional = cajeroRepository.findById(id);
        if (cajeroOptional.isPresent()) {
            Cajero cajeroDb = cajeroOptional.orElseThrow();
            cajeroDb.setNombre(cajero.getNombre());
            cajeroDb.setApellidoPaterno(cajero.getApellidoPaterno());
            cajeroDb.setApellidoMaterno(cajero.getApellidoMaterno());
            cajeroDb.setFechaNacimiento(cajero.getFechaNacimiento());
            cajeroDb.setGenero(cajero.getGenero());
            cajeroDb.setTelefono(cajero.getTelefono());
            cajeroDb.setEmail(cajero.getEmail());
            cajeroDb.setDireccion(cajero.getDireccion());
            cajeroDb.setRfc(cajero.getRfc());
            cajeroDb.setDiscapacidad(cajero.getDiscapacidad());
            cajeroDb.setEstadoCivil(cajero.getEstadoCivil());
            cajeroDb.setNivelDeEstudios(cajero.getNivelDeEstudios());
            cajeroDb.setFechaContratacion(cajero.getFechaContratacion());
            cajeroDb.setTurno(cajero.getTurno());
            cajeroDb.setAniosDeExperiencia(cajero.getAniosDeExperiencia());
            cajeroDb.setHorasDeTrabajo(cajero.getHorasDeTrabajo());
            cajeroDb.setEstado(cajero.getEstado());
            cajeroDb.setSucursal(cajero.getSucursal());
            cajeroDb.setResponsabilidades(cajero.getResponsabilidades());
            cajeroDb.setSueldo(cajero.getSueldo());
            cajeroDb.setCajaAsignada(cajero.getCajaAsignada());
            cajeroDb.setNumeroTransacciones(cajero.getNumeroTransacciones());

            return Optional.of(cajeroRepository.save(cajeroDb));
        }
        return Optional.empty();
    }

    @Transactional
    @Modifying
    @Override
    public Optional<Cajero> delete(Long id) {
        return cajeroRepository.findById(id).map(cajero -> {
            cajeroRepository.delete(cajero);
            return Optional.of(cajero);
        }).orElseGet(Optional::empty);
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean isCajaOcupada(Integer cajaAsignada, Long sucursalId) {
        return cajeroRepository.isCajaOcupada(cajaAsignada, sucursalId);
    }
}
