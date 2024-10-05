package com.ulsa.oaxaca.edu.proyecto_banco.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Personal;
import com.ulsa.oaxaca.edu.proyecto_banco.service.PersonalService;

@RestController
@RequestMapping("/api/personal")
public class PersonalController {

    @Autowired
    private PersonalService personalService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Personal>> getAll() {
        List<Personal> personal = personalService.findAll();
        return ResponseEntity.ok(personal);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Personal> getById(@PathVariable Long id) {
        Optional<Personal> personal = personalService.findById(id);
        return personal.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
