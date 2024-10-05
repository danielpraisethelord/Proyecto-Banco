package com.ulsa.oaxaca.edu.proyecto_banco.controller;

import java.util.*;
import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Cliente;
import com.ulsa.oaxaca.edu.proyecto_banco.entities.Cuenta;
import com.ulsa.oaxaca.edu.proyecto_banco.entities.Tarjeta;
import com.ulsa.oaxaca.edu.proyecto_banco.service.ClienteService;
import com.ulsa.oaxaca.edu.proyecto_banco.service.CuentaService;
import com.ulsa.oaxaca.edu.proyecto_banco.service.TarjetaService;
import com.ulsa.oaxaca.edu.proyecto_banco.validation.EndpointsValidation;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tarjeta")
public class TarjetaController {

    @Autowired
    private TarjetaService tarjetaService;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private ClienteService clienteService;

    @PreAuthorize("hasRole('GERENTE', 'EJECUTIVO')")
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        List<Tarjeta> tarjetas = tarjetaService.findAll();
        return ResponseEntity.ok(tarjetas);
    }

    @PreAuthorize("hasRole('GERENTE', 'EJECUTIVO')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Tarjeta> tarjetaOptional = tarjetaService.findById(id);
        return tarjetaOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('GERENTE', 'EJECUTIVO')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody Tarjeta tarjeta, BindingResult result) {
        if (result.hasErrors()) {
            return EndpointsValidation.validation(result);
        }

        Optional<Cuenta> cuentaOptional = cuentaService.findById(tarjeta.getCuenta().getId());
        if (cuentaOptional.isPresent()) {
            tarjeta.setCuenta(cuentaOptional.get());
            Tarjeta tarjetaSave = tarjetaService.save(tarjeta);
            return ResponseEntity.status(HttpStatus.CREATED).body(tarjetaSave);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cuenta no encontrada.");
        }
    }

    @PreAuthorize("hasRole('GERENTE', 'EJECUTIVO')")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Tarjeta tarjeta, BindingResult result) {
        if (result.hasErrors()) {
            return EndpointsValidation.validation(result);
        }
        Optional<Tarjeta> tarjetaData = tarjetaService.update(id, tarjeta);
        if (tarjetaData.isPresent()) {
            return ResponseEntity.ok(tarjetaData.orElseThrow());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('GERENTE', 'EJECUTIVO')")
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> partialUpdate(@PathVariable Long id, @PathVariable Map<String, Object> updates,
            BindingResult result) {
        ResponseEntity<?> validationRequest = EndpointsValidation.validatePatchRequest(updates, Tarjeta.class);
        if (validationRequest.getStatusCode().is4xxClientError()) {
            return validationRequest;
        }
        Optional<Tarjeta> tarjetaOptional = tarjetaService.findById(id);
        if (tarjetaOptional.isPresent()) {
            Tarjeta tarjeta = tarjetaOptional.orElseThrow();
            updates.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Tarjeta.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, tarjeta, value);
                }
            });
            return ResponseEntity.ok(tarjetaService.save(tarjeta));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('GERENTE', 'EJECUTIVO')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Tarjeta> tarjetaOptional = tarjetaService.delete(id);
        if (tarjetaOptional.isPresent()) {
            return ResponseEntity.ok(tarjetaOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping("/simulatePurchase/{id}")
    public ResponseEntity<?> simulatePurchase(@PathVariable Long id, @RequestBody Map<String, Object> purchaseDetails,
            Authentication authentication) {
        String currentUsername = authentication.getName();

        Optional<Tarjeta> tarjetaOptional = tarjetaService.findById(id);
        if (!tarjetaOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Tarjeta tarjeta = tarjetaOptional.get();
        Long idCuenta = tarjeta.getCuenta().getId();
        Optional<Cuenta> cuentaOptional = cuentaService.findById(idCuenta);

        if (!cuentaOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Cuenta asociada a la tarjeta no encontrada.");
        }

        Cuenta cuenta = cuentaOptional.get();
        Long idCliente = cuenta.getCliente().getId();
        Optional<Cliente> clienteOptional = clienteService.findById(idCliente);

        if (!clienteOptional.isPresent() || !clienteOptional.get().getRfc().equals(currentUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No tienes permiso para realizar esta operación.");
        }

        double amount = Double.parseDouble(purchaseDetails.get("amount").toString());
        if (tarjeta.getLimiteCredito() >= amount) {
            tarjeta.setSaldoActual(tarjeta.getSaldoActual() + amount);
            tarjeta.setLimiteCredito(tarjeta.getLimiteCredito() - amount);

            // Log to check updated values before saving
            System.out.println("Updated balance: " + tarjeta.getSaldoActual());
            System.out.println("Updated credit limit: " + tarjeta.getLimiteCredito());

            tarjetaService.update(id, tarjeta);
            return ResponseEntity.ok("Purchase successful. New balance: " + tarjeta.getSaldoActual());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient balance.");
        }
    }
}