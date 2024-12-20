package com.ulsa.oaxaca.edu.proyecto_banco.serviceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Cliente;
import com.ulsa.oaxaca.edu.proyecto_banco.entities.Cuenta;
import com.ulsa.oaxaca.edu.proyecto_banco.entities.User;
import com.ulsa.oaxaca.edu.proyecto_banco.repositories.ClienteRepository;
import com.ulsa.oaxaca.edu.proyecto_banco.repositories.RoleRepository;
import com.ulsa.oaxaca.edu.proyecto_banco.service.ClienteService;
import com.ulsa.oaxaca.edu.proyecto_banco.service.CuentaService;
import com.ulsa.oaxaca.edu.proyecto_banco.service.UserService;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public Cliente save(Cliente cliente) {
        cliente.setFechaRegistro(LocalDate.now());
        cliente.setEstado("Activo");
        Cuenta cuenta = Cuenta.builder()
                .tipoCuenta("Cuenta de ahorro")
                .moneda("MXN")
                .cliente(cliente)
                .build();

        cuentaService.save(cuenta);

        cliente.setCuentas(new ArrayList<>());
        cliente.getCuentas().add(cuenta);

        Cliente clienteDb = clienteRepository.save(cliente);

        User user = User.builder()
                .username(clienteDb.getRfc())
                .password(passwordEncoder.encode(cliente.getPassword()))
                .role(roleRepository.findByName("ROLE_CLIENTE").orElseThrow())
                .persona(clienteDb)
                .build();

        userService.save(user);

        return clienteDb;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Cliente> findAll() {
        return (List<Cliente>) clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    @Transactional
    @Override
    @Modifying
    public Optional<Cliente> update(Long id, Cliente cliente) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isPresent()) {
            Cliente clienteDb = clienteOptional.orElseThrow();
            clienteDb.setNombre(cliente.getNombre());
            clienteDb.setApellidoPaterno(cliente.getApellidoPaterno());
            clienteDb.setApellidoMaterno(cliente.getApellidoMaterno());
            clienteDb.setFechaNacimiento(cliente.getFechaNacimiento());
            clienteDb.setGenero(cliente.getGenero());
            clienteDb.setTelefono(cliente.getTelefono());
            clienteDb.setEmail(cliente.getEmail());
            clienteDb.setDireccion(cliente.getDireccion());
            clienteDb.setRfc(cliente.getRfc());
            clienteDb.setDiscapacidad(cliente.getDiscapacidad());
            clienteDb.setEstadoCivil(cliente.getEstadoCivil());
            clienteDb.setNivelDeEstudios(cliente.getNivelDeEstudios());
            clienteDb.setFechaRegistro(cliente.getFechaRegistro());
            clienteDb.setEstado(cliente.getEstado());
            clienteDb.setSucursal(cliente.getSucursal());
            clienteDb.setCuentas(cliente.getCuentas());
            clienteDb.setTransacciones(cliente.getTransacciones());
            return Optional.of(clienteRepository.save(clienteDb));
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    @Modifying
    public Optional<Cliente> delete(Long id) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        clienteOptional.ifPresent(clienteRepository::delete);
        return clienteOptional;
    }

    @Override
    public Optional<Cliente> findByRfc(String rfc) {
        return clienteRepository.findByRfc(rfc);
    }

}
