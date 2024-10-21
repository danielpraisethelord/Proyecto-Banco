package com.ulsa.oaxaca.edu.proyecto_banco.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Ejecutivo;
import com.ulsa.oaxaca.edu.proyecto_banco.entities.User;
import com.ulsa.oaxaca.edu.proyecto_banco.repositories.EjecutivoRepository;
import com.ulsa.oaxaca.edu.proyecto_banco.repositories.RoleRepository;
import com.ulsa.oaxaca.edu.proyecto_banco.service.EjecutivoService;
import com.ulsa.oaxaca.edu.proyecto_banco.service.UserService;

@Service
public class EjecutivoServiceImpl implements EjecutivoService {

    @Autowired
    private EjecutivoRepository ejecutivoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public Ejecutivo save(Ejecutivo ejecutivo) {
        Ejecutivo ejecutivoDb = ejecutivoRepository.save(ejecutivo);

        User user = User.builder()
                .username(ejecutivo.getRfc())
                .password(passwordEncoder.encode(ejecutivo.getPassword()))
                .role(roleRepository.findByName("ROLE_EJECUTIVO").orElseThrow())
                .persona(ejecutivoDb)
                .build();

        userService.save(user);

        return ejecutivoDb;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Ejecutivo> findAll() {
        return (List<Ejecutivo>) ejecutivoRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Ejecutivo> findById(Long id) {
        return ejecutivoRepository.findById(id);
    }

    @Transactional
    @Modifying
    @Override
    public Optional<Ejecutivo> update(Long id, Ejecutivo ejecutivo) {
        Optional<Ejecutivo> ejecutivoOptional = ejecutivoRepository.findById(id);
        if (ejecutivoOptional.isPresent()) {
            Ejecutivo ejecutiboDB = ejecutivoOptional.orElseThrow();
            ejecutiboDB.setNombre(ejecutivo.getNombre());
            ejecutiboDB.setApellidoPaterno(ejecutivo.getApellidoPaterno());
            ejecutiboDB.setApellidoMaterno(ejecutivo.getApellidoMaterno());
            ejecutiboDB.setFechaNacimiento(ejecutivo.getFechaNacimiento());
            ejecutiboDB.setGenero(ejecutivo.getGenero());
            ejecutiboDB.setTelefono(ejecutivo.getTelefono());
            ejecutiboDB.setEmail(ejecutivo.getEmail());
            ejecutiboDB.setDireccion(ejecutivo.getDireccion());
            ejecutiboDB.setRfc(ejecutivo.getRfc());
            ejecutiboDB.setFechaContratacion(ejecutivo.getFechaContratacion());
            ejecutiboDB.setTurno(ejecutivo.getTurno());
            ejecutiboDB.setAniosDeExperiencia(ejecutivo.getAniosDeExperiencia());
            ejecutiboDB.setHorasDeTrabajo(ejecutivo.getHorasDeTrabajo());
            ejecutiboDB.setEstado(ejecutivo.getEstado());
            ejecutiboDB.setSucursal(ejecutivo.getSucursal());
            ejecutiboDB.setResponsabilidades(ejecutivo.getResponsabilidades());
            ejecutiboDB.setSueldo(ejecutivo.getSueldo());
            ejecutiboDB.setEstiloDeClientesAsignados(ejecutivo.getEstiloDeClientesAsignados());
            ejecutiboDB.setObjetivosDeVentas(ejecutivo.getObjetivosDeVentas());
            ejecutiboDB.setDepartamento(ejecutivo.getDepartamento());

            return Optional.of(ejecutivoRepository.save(ejecutiboDB));
        }
        return Optional.empty();
    }

    @Transactional
    @Modifying
    @Override
    public Optional<Ejecutivo> delete(Long id) {
        Optional<Ejecutivo> ejecutivoOptional = ejecutivoRepository.findById(id);
        ejecutivoOptional.ifPresent(ejecutivoRepository::delete);
        return ejecutivoOptional;
    }
}
