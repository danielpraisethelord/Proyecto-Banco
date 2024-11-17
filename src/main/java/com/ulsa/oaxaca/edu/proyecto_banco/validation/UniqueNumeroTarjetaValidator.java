package com.ulsa.oaxaca.edu.proyecto_banco.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ulsa.oaxaca.edu.proyecto_banco.service.TarjetaService;
import com.ulsa.oaxaca.edu.proyecto_banco.utils.TarjetaYaExisteException;

@Component
public class UniqueNumeroTarjetaValidator implements ConstraintValidator<UniqueNumeroTarjeta, String> {

    @Autowired
    private TarjetaService tarjetaService;

    @Override
    public boolean isValid(String numeroTarjeta, ConstraintValidatorContext context) {
        if (tarjetaService == null) {
            return true;
        }

        if (numeroTarjeta == null) {
            return true;
        }

        if (tarjetaService.existsByNumeroTarjeta(numeroTarjeta)) {
            throw new TarjetaYaExisteException("El número de tarjeta ya existe");
        }
        return true;
    }
}