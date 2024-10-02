package com.ulsa.oaxaca.edu.proyecto_banco.validation;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindingResult;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class EndpointsValidation {

    public static ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " +
                    err.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

    public static <T> ResponseEntity<?> validatePatchRequest(Map<String, Object> updates, Class<T> entityClass) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Map<String, String> errors = new HashMap<>();

        try {
            T instance = entityClass.getDeclaredConstructor().newInstance();

            updates.forEach((key, value) -> {
                try {
                    Field field = ReflectionUtils.findField(entityClass, key);
                    if (field != null) {
                        field.setAccessible(true);
                        ReflectionUtils.setField(field, instance, value);

                        Set<ConstraintViolation<T>> violations = validator.validateProperty(instance, key);
                        for (ConstraintViolation<T> violation : violations) {
                            errors.put(key, violation.getMessage());
                        }
                    } else {
                        errors.put(key, "Campo no encontrado: " + key);
                    }
                } catch (Exception e) {
                    errors.put(key, "Error al validar el campo " + key + ": " + e.getMessage());
                }
            });

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear la instancia de la entidad: " + e.getMessage());
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        return ResponseEntity.ok().build();
    }
}