package com.ulsa.oaxaca.edu.proyecto_banco.controller;

import com.ulsa.oaxaca.edu.proyecto_banco.dto.CuentaDto;
import com.ulsa.oaxaca.edu.proyecto_banco.entities.Cliente;
import com.ulsa.oaxaca.edu.proyecto_banco.entities.Cuenta;
import com.ulsa.oaxaca.edu.proyecto_banco.entities.Tarjeta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
// import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

import com.ulsa.oaxaca.edu.proyecto_banco.service.ClienteService;
import com.ulsa.oaxaca.edu.proyecto_banco.service.CuentaService;
import com.ulsa.oaxaca.edu.proyecto_banco.service.TarjetaService;
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

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private TarjetaService tarjetaService;

    @PreAuthorize("hasRole('GERENTE', 'EJECUTIVO')")
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        List<Cuenta> cuentas = cuentaService.findAll();
        return ResponseEntity.ok(cuentas);
    }

    @PreAuthorize("hasRole('GERENTE', 'EJECUTIVO', 'CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, Authentication authentication) {
        String currentUsername = authentication.getName();
        Optional<Cuenta> cuentaOptional = cuentaService.findById(id);

        if (!cuentaOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Cuenta cuenta = cuentaOptional.get();
        Long idCliente = cuenta.getCliente().getId();
        Optional<Cliente> clienteOptional = clienteService.findById(idCliente);

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))) {
            if (!clienteOptional.isPresent() || !clienteOptional.get().getRfc().equals(currentUsername)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("No tienes permiso para acceder a este recurso.");
            }
        }

        return ResponseEntity.ok(cuenta);
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

    @PreAuthorize("hasRole('GERENTE', 'EJECUTIVO')")
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

    @PreAuthorize("hasRole('GERENTE', 'EJECUTIVO')")
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

    @PreAuthorize("hasRole('GERENTE', 'EJECUTIVO')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Cuenta> cuentaOptional = cuentaService.delete(id);
        if (cuentaOptional.isPresent()) {
            return ResponseEntity.ok(cuentaOptional.orElseThrow());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping("/depositar")
    public ResponseEntity<?> depositar(@RequestBody Map<String, Object> request, Authentication authentication) {
        Long id = Long.valueOf(request.get("id").toString());
        Double monto = Double.valueOf(request.get("monto").toString());
        String currentUsername = authentication.getName();

        Optional<Cuenta> cuentaOptional = cuentaService.findById(id);
        if (!cuentaOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Cuenta no encontrada.");
        }

        Cuenta cuenta = cuentaOptional.get();
        Long idCliente = cuenta.getCliente().getId();
        Optional<Cliente> clienteOptional = clienteService.findById(idCliente);

        if (!clienteOptional.isPresent() || !clienteOptional.get().getRfc().equals(currentUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No tienes permiso para realizar esta operación.");
        }

        Optional<CuentaDto> cuentaDtoOptional = cuentaService.depositar(id, monto);
        if (cuentaDtoOptional.isPresent()) {
            return ResponseEntity.ok(cuentaDtoOptional.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping("/retirar")
    public ResponseEntity<?> retirar(@RequestBody Map<String, Object> request, Authentication authentication) {
        Long id = Long.valueOf(request.get("id").toString());
        Double monto = Double.valueOf(request.get("monto").toString());
        String currentUsername = authentication.getName();

        Optional<Cuenta> cuentaOptional = cuentaService.findById(id);
        if (!cuentaOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Cuenta no encontrada.");
        }

        Cuenta cuenta = cuentaOptional.get();
        Long idCliente = cuenta.getCliente().getId();
        Optional<Cliente> clienteOptional = clienteService.findById(idCliente);

        if (!clienteOptional.isPresent() || !clienteOptional.get().getRfc().equals(currentUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No tienes permiso para realizar esta operación.");
        }

        return cuentaService.retirar(id, monto);
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestBody Map<String, Object> request, Authentication authentication) {
        Long idOrigen = Long.valueOf(request.get("idOrigen").toString());
        Long idDestino = Long.valueOf(request.get("idDestino").toString());
        Double monto = Double.valueOf(request.get("monto").toString());
        String currentUsername = authentication.getName();

        Optional<Cuenta> cuentaOrigenOptional = cuentaService.findById(idOrigen);
        if (!cuentaOrigenOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Cuenta de origen no encontrada.");
        }

        Cuenta cuentaOrigen = cuentaOrigenOptional.get();
        Long idCliente = cuentaOrigen.getCliente().getId();
        Optional<Cliente> clienteOptional = clienteService.findById(idCliente);

        if (!clienteOptional.isPresent() || !clienteOptional.get().getRfc().equals(currentUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No tienes permiso para realizar esta operación.");
        }

        return cuentaService.transferir(idOrigen, idDestino, monto);
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping("/pagar")
    public ResponseEntity<?> pagar(@RequestBody Map<String, Object> request, Authentication authentication) {
        Long idTarjeta = Long.valueOf(request.get("idTarjeta").toString());
        Double monto = Double.valueOf(request.get("monto").toString());
        String currentUsername = authentication.getName();

        Optional<Tarjeta> tarjetaOptional = tarjetaService.findById(idTarjeta);
        if (!tarjetaOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Tarjeta no encontrada.");
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

        return cuentaService.pagar(idTarjeta, monto);
    }
}
