package com.ulsa.oaxaca.edu.proyecto_banco.controller;

import java.util.Optional;
import java.util.Map;
import java.lang.reflect.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindingResult;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Sucursal;
import com.ulsa.oaxaca.edu.proyecto_banco.service.SucursalService;
import com.ulsa.oaxaca.edu.proyecto_banco.validation.EndpointsValidation;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sucursal")
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody Sucursal sucursal, BindingResult result) {
        if (result.hasErrors()) {
            return EndpointsValidation.validation(result);
        }
        Sucursal sucursalSave = sucursalService.save(sucursal);
        if (sucursalSave.getId() != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(sucursalSave);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(sucursalService.findAll());
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Sucursal> sucursal = sucursalService.findById(id);
        if (sucursal.isPresent()) {
            return ResponseEntity.ok(sucursal.orElseThrow());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Sucursal sucursal,
            BindingResult result) {
        if (result.hasErrors()) {
            return EndpointsValidation.validation(result);
        }
        Optional<Sucursal> sucursalOptional = sucursalService.update(id, sucursal);
        if (sucursalOptional.isPresent()) {
            return ResponseEntity.ok(sucursalOptional.orElseThrow());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Sucursal> sucursal = sucursalService.delete(id);
        if (sucursal.isPresent()) {
            return ResponseEntity.ok(sucursal.orElseThrow());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> updates,
            BindingResult result) {
        ResponseEntity<?> validationResponse = EndpointsValidation.validatePatchRequest(updates, Sucursal.class);
        if (validationResponse.getStatusCode().is4xxClientError()) {
            return validationResponse;
        }
        Optional<Sucursal> sucursalOptional = sucursalService.findById(id);
        if (sucursalOptional.isPresent()) {
            Sucursal sucursalDb = sucursalOptional.orElseThrow();
            updates.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Sucursal.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, sucursalDb, value);
                }
            });
            sucursalService.save(sucursalDb);
            return ResponseEntity.ok(sucursalDb);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
