package com.ulsa.oaxaca.edu.proyecto_banco.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindingResult;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Persona;
import com.ulsa.oaxaca.edu.proyecto_banco.service.PersonaService;
import com.ulsa.oaxaca.edu.proyecto_banco.validation.EndpointsValidation;

import jakarta.validation.Valid;

import java.util.*;
import java.lang.reflect.*;

@RestController
@RequestMapping("/api/persona")
public class PersonaController {

    @Autowired
    PersonaService personaService;

    @PostMapping("/create")
    public ResponseEntity<Persona> create(@RequestBody Persona persona) {
        Persona personaSave = personaService.save(persona);
        return ResponseEntity.status(HttpStatus.CREATED).body(personaSave);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Persona>> getAll() {
        List<Persona> personas = personaService.findAll();
        return ResponseEntity.ok(personas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persona> getById(@PathVariable Long id) {
        Optional<Persona> persona = personaService.findById(id);
        return persona.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Persona persona, BindingResult result) {
        if (result.hasErrors()) {
            return EndpointsValidation.validation(result);
        }
        Optional<Persona> personaData = personaService.update(id, persona);
        if (personaData.isPresent()) {
            return ResponseEntity.ok(personaData.orElseThrow());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Persona> persona = personaService.delete(id);
        if (persona.isPresent()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> updates,
            BindingResult result) {
        ResponseEntity<?> validationResponse = EndpointsValidation.validatePatchRequest(updates, Persona.class);
        if (validationResponse.getStatusCode().is4xxClientError()) {
            return validationResponse;
        }
        Optional<Persona> personaOptional = personaService.findById(id);
        if (personaOptional.isPresent()) {
            Persona personaDb = personaOptional.orElseThrow();
            updates.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Persona.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, personaDb, value);
                }
            });
            personaService.save(personaDb);
            return ResponseEntity.ok(personaDb);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
