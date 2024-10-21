package com.ulsa.oaxaca.edu.proyecto_banco.controller;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Gerente;
import com.ulsa.oaxaca.edu.proyecto_banco.repositories.BanamexOaxacaRepository;
import com.ulsa.oaxaca.edu.proyecto_banco.service.GerenteService;
import com.ulsa.oaxaca.edu.proyecto_banco.validation.EndpointsValidation;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/gerente")
public class GerenteController {

    @Autowired
    private GerenteService gerenteService;

    @Autowired
    private BanamexOaxacaRepository banamexOaxacaRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Gerente>> getAll() {
        List<Gerente> gerentes = gerenteService.findAll();
        return ResponseEntity.ok(gerentes);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Gerente> getById(@PathVariable Long id) {
        Optional<Gerente> gerente = gerenteService.findById(id);
        return gerente.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody Gerente gerente, BindingResult result) {
        if (result.hasErrors()) {
            return EndpointsValidation.validation(result);
        }

        Long sucursalId = gerente.getSucursal().getId();

        if (gerenteService.existsBySucursalId(sucursalId)) {
            return new ResponseEntity<>(
                    Collections.singletonMap("mensaje", "Ya existe un gerente para esta sucursal"),
                    HttpStatus.BAD_REQUEST);
        }
        banamexOaxacaRepository.incrementNumeroEmpleados(1L);
        Gerente gerenteSave = gerenteService.save(gerente);
        return ResponseEntity.status(HttpStatus.CREATED).body(gerenteSave);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Gerente gerente,
            BindingResult result) {
        if (result.hasErrors()) {
            return EndpointsValidation.validation(result);
        }
        Optional<Gerente> gerenteData = gerenteService.update(id, gerente);
        if (gerenteData.isPresent()) {
            return ResponseEntity.ok(gerenteData.orElseThrow());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> updates,
            BindingResult result) {
        ResponseEntity<?> validationResponse = EndpointsValidation.validatePatchRequest(updates, Gerente.class);
        if (validationResponse.getStatusCode().is4xxClientError()) {
            return validationResponse;
        }
        Optional<Gerente> gerenteOptional = gerenteService.findById(id);
        if (gerenteOptional.isPresent()) {
            Gerente gerenteDb = gerenteOptional.orElseThrow();
            updates.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Gerente.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, gerenteDb, value);
                }
            });
            gerenteService.save(gerenteDb);
            return ResponseEntity.ok(gerenteDb);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Gerente> delete(@PathVariable Long id) {
        Optional<Gerente> gerente = gerenteService.delete(id);
        if (gerente.isPresent()) {
            return ResponseEntity.ok(gerente.orElseThrow());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}