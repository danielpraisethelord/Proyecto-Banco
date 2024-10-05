package com.ulsa.oaxaca.edu.proyecto_banco.controller;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Cajero;
import com.ulsa.oaxaca.edu.proyecto_banco.entities.Sucursal;
import com.ulsa.oaxaca.edu.proyecto_banco.repositories.SucursalRepository;
import com.ulsa.oaxaca.edu.proyecto_banco.service.CajeroSerice;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cajero")
public class CajeroController {

    @Autowired
    private CajeroSerice cajeroSerice;

    @Autowired
    private SucursalRepository sucursalRepository;

    @PreAuthorize("hasRole('GERENTE')")
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        List<Cajero> cajeros = cajeroSerice.findAll();
        return ResponseEntity.ok(cajeros);
    }

    @PreAuthorize("hasRole('GERENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Cajero> cajeroOptional = cajeroSerice.findById(id);
        return cajeroOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody Cajero cajero, BindingResult result) {
        if (result.hasErrors()) {
            return EndpointsValidation.validation(result);
        }

        Optional<Sucursal> sucursalOptional = sucursalRepository.findById(cajero.getSucursal().getId());
        if (!sucursalOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La sucursal no existe.");
        }

        Boolean isOcupada = cajeroSerice.isCajaOcupada(cajero.getCajaAsignada(), cajero.getSucursal().getId());
        if (isOcupada) {
            return ResponseEntity.badRequest().body("La caja asignada ya esta ocupada");
        }

        Cajero cajeroSave = cajeroSerice.save(cajero);
        return ResponseEntity.status(HttpStatus.CREATED).body(cajeroSave);
    }

    @PreAuthorize("hasRole('GERENTE')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Cajero cajero, BindingResult result) {
        if (result.hasErrors()) {
            return EndpointsValidation.validation(result);
        }
        Optional<Cajero> cajeroOptional = cajeroSerice.update(id, cajero);
        if (cajeroOptional.isPresent()) {
            return ResponseEntity.ok(cajeroOptional.orElseThrow());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('GERENTE')")
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> updates,
            BindingResult result) {
        ResponseEntity<?> validationResponse = EndpointsValidation.validatePatchRequest(updates, Cajero.class);
        if (validationResponse.getStatusCode().is4xxClientError()) {
            return validationResponse;
        }
        Optional<Cajero> cajeroOptional = cajeroSerice.findById(id);
        if (cajeroOptional.isPresent()) {
            Cajero cajeroDb = cajeroOptional.orElseThrow();
            updates.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Cajero.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, cajeroDb, value);
                }
            });
            cajeroSerice.save(cajeroDb);
            return ResponseEntity.ok(cajeroDb);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('GERENTE')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Cajero> cajero = cajeroSerice.delete(id);
        if (cajero.isPresent()) {
            return ResponseEntity.ok(cajero.orElseThrow());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
