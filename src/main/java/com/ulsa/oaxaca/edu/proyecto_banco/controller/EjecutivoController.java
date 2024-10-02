package com.ulsa.oaxaca.edu.proyecto_banco.controller;

import java.util.*;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Ejecutivo;
import com.ulsa.oaxaca.edu.proyecto_banco.service.EjecutivoService;
import com.ulsa.oaxaca.edu.proyecto_banco.validation.EndpointsValidation;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ejecutivo")
public class EjecutivoController {

    @Autowired
    private EjecutivoService ejecutivoService;

    @GetMapping("/all")
    public ResponseEntity<List<Ejecutivo>> getAll() {
        List<Ejecutivo> ejecutivos = ejecutivoService.findAll();
        return ResponseEntity.ok(ejecutivos);
    }

    @GetMapping("{id}")
    public ResponseEntity<Ejecutivo> getById(@PathVariable Long id) {
        Optional<Ejecutivo> ejecutivo = ejecutivoService.findById(id);
        return ejecutivo.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody Ejecutivo ejecutivo, BindingResult result) {
        if (result.hasErrors()) {
            return EndpointsValidation.validation(result);
        }
        Ejecutivo ejecutivoSave = ejecutivoService.save(ejecutivo);
        return ResponseEntity.status(HttpStatus.CREATED).body(ejecutivoSave);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Ejecutivo ejecutivo,
            BindingResult result) {
        if (result.hasErrors()) {
            return EndpointsValidation.validation(result);
        }
        Optional<Ejecutivo> ejecutivoOptional = ejecutivoService.update(id, ejecutivo);
        if (ejecutivoOptional.isPresent()) {
            return ResponseEntity.ok(ejecutivoOptional.orElseThrow());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> partialUpdate(@PathVariable Long id,
            @RequestBody Map<String, Object> updates, BindingResult result) {
        ResponseEntity<?> validationResponse = EndpointsValidation.validatePatchRequest(updates, Ejecutivo.class);
        if (validationResponse.getStatusCode().is4xxClientError()) {
            return validationResponse;
        }
        Optional<Ejecutivo> ejecutivoOptional = ejecutivoService.findById(id);
        if (ejecutivoOptional.isPresent()) {
            Ejecutivo ejecutivoDb = ejecutivoOptional.orElseThrow();
            updates.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Ejecutivo.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, ejecutivoDb, value);
                }
            });
            ejecutivoService.save(ejecutivoDb);
            return ResponseEntity.ok(ejecutivoDb);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete({id})")
    public ResponseEntity<Ejecutivo> delete(@PathVariable Long id) {
        Optional<Ejecutivo> ejecutivo = ejecutivoService.delete(id);
        if (ejecutivo.isPresent()) {
            return ResponseEntity.ok(ejecutivo.orElseThrow());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
