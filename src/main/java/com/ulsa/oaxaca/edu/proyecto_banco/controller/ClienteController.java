package com.ulsa.oaxaca.edu.proyecto_banco.controller;

import com.ulsa.oaxaca.edu.proyecto_banco.dto.ClienteDto;
import com.ulsa.oaxaca.edu.proyecto_banco.entities.Cliente;
import com.ulsa.oaxaca.edu.proyecto_banco.repositories.BanamexOaxacaRepository;
import com.ulsa.oaxaca.edu.proyecto_banco.service.ClienteService;
import com.ulsa.oaxaca.edu.proyecto_banco.utils.ClienteMapper;
import com.ulsa.oaxaca.edu.proyecto_banco.validation.EndpointsValidation;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private BanamexOaxacaRepository banamexOaxacaRepository;

    @PreAuthorize("hasAnyRole('GERENTE', 'EJECUTIVO')")
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        List<Cliente> clientes = clienteService.findAll();
        List<ClienteDto> clientesDto = clientes.stream()
                .map(ClienteMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientesDto);
    }

    @PreAuthorize("hasAnyRole('GERENTE', 'EJECUTIVO')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, Authentication authentication) {
        String currentUsername = authentication.getName();
        Optional<Cliente> clienteRfc = clienteService.findById(id);

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))) {
            if (!clienteRfc.isPresent() || !clienteRfc.get().getRfc().equals(currentUsername)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("No tienes permiso para acceder a este recurso.");
            }
        }

        Optional<Cliente> cliente = clienteService.findById(id);
        return cliente.map(value -> ResponseEntity.ok(ClienteMapper.toDto(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('GERENTE', 'EJECUTIVO')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {
        if (result.hasErrors()) {
            return EndpointsValidation.validation(result);
        }
        banamexOaxacaRepository.incrementTotalClientes(1L);
        Cliente clienteSave = clienteService.save(cliente);
        ClienteDto clienteDto = ClienteMapper.toDto(clienteSave);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteDto);
    }

    @PreAuthorize("hasAnyRole('GERENTE', 'EJECUTIVO')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Cliente cliente,
            BindingResult result) {
        if (result.hasErrors()) {
            return EndpointsValidation.validation(result);
        }
        Optional<Cliente> clienteData = clienteService.update(id, cliente);
        if (clienteData.isPresent()) {
            ClienteDto clienteDto = ClienteMapper.toDto(clienteData.orElseThrow());
            return ResponseEntity.ok(clienteDto);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('GERENTE', 'EJECUTIVO')")
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> updates,
            BindingResult result) {
        ResponseEntity<?> validationResponse = EndpointsValidation.validatePatchRequest(updates, Cliente.class);
        if (validationResponse.getStatusCode().is4xxClientError()) {
            return validationResponse;
        }
        Optional<Cliente> clienteOptional = clienteService.findById(id);
        if (clienteOptional.isPresent()) {
            Cliente clienteDb = clienteOptional.orElseThrow();
            updates.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Cliente.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, clienteDb, value);
                }
            });
            clienteService.save(clienteDb);
            ClienteDto clienteDto = ClienteMapper.toDto(clienteDb);
            return ResponseEntity.ok(clienteDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAnyRole('GERENTE', 'EJECUTIVO')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.delete(id);
        if (cliente.isPresent()) {
            return ResponseEntity.ok(cliente);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}