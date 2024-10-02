package com.ulsa.oaxaca.edu.proyecto_banco.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "cliente")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Cliente extends Persona {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "sucursal_id")
    @JsonIgnoreProperties({ "nombre", "direccion", "telefono", "email", "horarioAtencion", "fechaApertura", "estado",
            "banco", "personal", "clientes" })
    private Sucursal sucursal;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cuenta> cuentas;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaccion> transacciones;
}