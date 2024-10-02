package com.ulsa.oaxaca.edu.proyecto_banco.controller;

import com.ulsa.oaxaca.edu.proyecto_banco.dto.CuentaDto;
import com.ulsa.oaxaca.edu.proyecto_banco.entities.Cuenta;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
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

import com.ulsa.oaxaca.edu.proyecto_banco.service.CuentaService;
import com.ulsa.oaxaca.edu.proyecto_banco.validation.EndpointsValidation;

import jakarta.validation.Valid;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cuenta")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        List<Cuenta> cuentas = cuentaService.findAll();
        return ResponseEntity.ok(cuentas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Cuenta> cuentaOptional = cuentaService.findById(id);
        return cuentaOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // @PostMapping("/create")
    // public ResponseEntity<?> create(@Valid @RequestBody Cuenta cuenta,
    // BindingResult result) {
    // if (result.hasErrors()) {
    // return EndpointsValidation.validation(result);
    // }
    // Cuenta cuentaSave = cuentaService.save(cuenta);
    // return ResponseEntity.status(HttpStatus.CREATED).body(cuentaSave);
    // }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Cuenta cuenta, BindingResult result) {
        if (result.hasErrors()) {
            return EndpointsValidation.validation(result);
        }
        Optional<Cuenta> cuentaData = cuentaService.update(id, cuenta);
        if (cuentaData.isPresent()) {
            return ResponseEntity.ok(cuentaData.orElseThrow());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> updates,
            BindingResult result) {
        ResponseEntity<?> validationResponse = EndpointsValidation.validatePatchRequest(updates, Cuenta.class);
        if (validationResponse.getStatusCode().is4xxClientError()) {
            return validationResponse;
        }
        Optional<Cuenta> cuentaOptional = cuentaService.findById(id);
        if (cuentaOptional.isPresent()) {
            Cuenta cuentaDb = cuentaOptional.orElseThrow();
            updates.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Cuenta.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, cuentaDb, value);
                }
            });
            return ResponseEntity.ok(cuentaService.save(cuentaDb));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Cuenta> cuentaOptional = cuentaService.delete(id);
        if (cuentaOptional.isPresent()) {
            return ResponseEntity.ok(cuentaOptional.orElseThrow());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/depositar")
    public ResponseEntity<?> depositar(@RequestBody Map<String, Object> request) {
        Long id = Long.valueOf(request.get("id").toString());
        Double monto = Double.valueOf(request.get("monto").toString());
        Optional<CuentaDto> cunetaDtoOptional = cuentaService.depositar(id, monto);
        if (cunetaDtoOptional.isPresent()) {
            return ResponseEntity.ok(cunetaDtoOptional.orElseThrow());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/retirar")
    public ResponseEntity<?> retirar(@RequestBody Map<String, Object> request) {
        Long id = Long.valueOf(request.get("id").toString());
        Double monto = Double.valueOf(request.get("monto").toString());
        return cuentaService.retirar(id, monto);
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestBody Map<String, Object> request) {
        Long idOrigen = Long.valueOf(request.get("idOrigen").toString());
        Long idDestino = Long.valueOf(request.get("idDestino").toString());
        Double monto = Double.valueOf(request.get("monto").toString());
        return cuentaService.transferir(idOrigen, idDestino, monto);
    }

    @PostMapping("/pagar")
    public ResponseEntity<?> pagar(@RequestBody Map<String, Object> request) {
        Long idTarjeta = Long.valueOf(request.get("idTarjeta").toString());
        Double monto = Double.valueOf(request.get("monto").toString());
        return cuentaService.pagar(idTarjeta, monto);
    }
}
