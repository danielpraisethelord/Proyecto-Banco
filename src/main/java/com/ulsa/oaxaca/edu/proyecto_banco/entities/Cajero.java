package com.ulsa.oaxaca.edu.proyecto_banco.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "cajero")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Cajero extends Personal {

    @NotNull(message = "La caja asignada es obligatoria")
    @Column(name = "caja_asignada")
    private Integer cajaAsignada;

    @Column(name = "numero_transacciones")
    private Integer numeroTransacciones;
}