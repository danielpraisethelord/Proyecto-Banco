package com.ulsa.oaxaca.edu.proyecto_banco.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueNumeroTarjetaValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueNumeroTarjeta {
    String message() default "El número de tarjeta ya existe";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}