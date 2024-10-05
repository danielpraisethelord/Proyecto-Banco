package com.ulsa.oaxaca.edu.proyecto_banco.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrantedAuthorityJsonCreator {

    @JsonCreator
    public SimpleGrantedAuthorityJsonCreator(@JsonProperty("authority") String role) {
    }
}
